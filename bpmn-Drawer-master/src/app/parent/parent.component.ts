import { Component } from '@angular/core';
import * as FileSaver from 'file-saver';
import { HttpClient } from '@angular/common/http';
import BpmnJS from 'bpmn-js/dist/bpmn-modeler.production.min.js';

@Component({
  selector: 'app-parent',
  templateUrl: './parent.component.html',
  styleUrls: ['./parent.component.css']
})
export class ParentComponent {


  constructor(private http: HttpClient) { }
  compUrl = './assets/create.xml';

  importError?: Error;

  handleImported(event: Event) {

  }

  downloadImageAsXml(imageUrl: string, fileName: string, directoryPath: string): void {
    this.http.get(imageUrl, { responseType: 'blob' })
      .subscribe((blob: Blob) => {
        const file = new File([blob], `${fileName}.xml`, { type: 'text/xml' });
        FileSaver.saveAs(file, `${directoryPath}/${fileName}.xml`);
      });
  }
  bpmnData = '<bpmn ...></bpmn>';

  downloadBpmnXml() {
    this.generateBpmnXml(this.bpmnData);
  }

  generateBpmnXml(bpmnData) {
    const bpmnJS = new BpmnJS();
    bpmnJS.importXML(bpmnData, (err) => {
      if (err) {
        console.error(err);
      } else {
        bpmnJS.saveXML((err, xml) => {
          if (err) {
            console.error(err);
          } else {
            const blob = new Blob([xml], { type: 'text/xml' });
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'bpmn.xml';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
          }
        });
      }
    });
  }
}





