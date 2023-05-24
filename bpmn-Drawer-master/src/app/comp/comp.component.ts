import {
  AfterContentInit,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
  SimpleChanges,
  EventEmitter
} from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map, switchMap } from 'rxjs/operators';
import * as BpmnJS from 'bpmn-js/dist/bpmn-modeler.production.min.js';

import { from, Observable, Subscription } from 'rxjs';


@Component({
  selector: 'app-comp',
  templateUrl: './comp.component.html',
  styleUrls: ['./comp.component.css']
})
export class CompComponent implements AfterContentInit, OnChanges, OnDestroy, OnInit {

  imageName = '';
  saveStatus = ''; // Variable to track the save status
  fileId: any | undefined;

  @ViewChild('ref', { static: true }) private el: ElementRef | undefined;
  @Input() public url?: string;
  @Output() private importDone: EventEmitter<any> = new EventEmitter();
  private bpmnJS: any = new BpmnJS();
  accessIdValue(id: any) {

    if (id) {
      this.fileId = id;
      this.url = 'http://localhost:8080/files/' + id.toString();
      console.log(this.url);
    }
    else {
      console.log("new");
    }
  }
  constructor(private http: HttpClient, private route: ActivatedRoute) {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.accessIdValue(id);

    }),
      this.bpmnJS.on('import.done', ({ error }) => {
        if (!error) {
          this.bpmnJS.get('canvas').zoom('fit-viewport');
        }
      });
  }

  ngAfterContentInit(): void {
    this.bpmnJS.attachTo(this.el.nativeElement);
  }

  ngOnInit(): void {
    if (this.url) {
      this.loadUrl(this.url);
    }
  }

  ngOnChanges(changes: SimpleChanges) {

    if (changes['url']) {
      this.loadUrl(changes['url'].currentValue);
    }
  }

  ngOnDestroy(): void {
    this.bpmnJS.destroy();
  }

  loadUrl(url: string): Subscription {
    console.log(url);
    return (
      this.http.get(url, { responseType: 'text' }).pipe(
        switchMap((xml: string) => this.importDiagram(xml)),
        map(result => result.warnings),
      ).subscribe(
        (warnings: any) => {
          this.importDone.emit({
            type: 'success',
            warnings
          });
        },
        (err: any) => {
          this.importDone.emit({
            type: 'error',
            error: err
          });
        }
      )
    );
  }


  private importDiagram(xml: string): Observable<{ warnings: Array<any> }> {
    return from(this.bpmnJS.importXML(xml) as Promise<{ warnings: Array<any> }>);
  }

  async saveXML() {
    const { xml } = await this.bpmnJS.saveXML({ format: true });
    console.log(xml);
    var blob = new Blob([xml], { type: "blob" });
    const fileName = this.imageName || 'diagram';


    if (this.fileId) {
      const updateUrl = `http://localhost:8080/files/${this.fileId}`;
      console.log(updateUrl)
      let formData: FormData = new FormData();
      formData.append("file", blob, fileName);
      console.log(formData);
      await this.http.put(updateUrl, formData).subscribe((response: any) => {
        console.log(response);
        alert('updated');
        this.saveStatus = 'Updated';
      });
    }
    else {
      let testData: FormData = new FormData();
      testData.append("file", blob, fileName);
      // testData.append("name", fileName);
      console.log(testData);

      this.saveStatus = 'Saving...';
      await this.http.post("http://localhost:8080/upload", testData).subscribe((response: any) => {
        console.log(response);
        alert("saved");
        this.saveStatus = 'Saved';
      });
    }
  }


}