package com.bpmn.drawer.DrawerController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bpmn.drawer.DrawerDTO.ResponseMessage;
import com.bpmn.drawer.DrawerDTO.ResponseFile;
import com.bpmn.drawer.DrawerService.DrawerService;
import com.bpmn.drawer.entity.File;

@Controller
@CrossOrigin(origins = "*")
public class DrawerController {

	  @Autowired
	  private DrawerService drawerService;

	  @PostMapping(value="/upload",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestBody MultipartFile file) {
	    String message = "";
	    try {
	      drawerService.store(file);

	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }
	  
	  @GetMapping("/files/{id}")
	  public ResponseEntity<byte[]> getFile(@PathVariable Integer id) {
	    File file = drawerService.getFile(id);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
	        .body(file.getData());
	  }
	  @GetMapping("/files")
	  public ResponseEntity<List<ResponseFile>> getListFiles() {
	    List<ResponseFile> files = drawerService.getAllFiles().map(File -> {
	      String fileDownloadUri = ServletUriComponentsBuilder
	          .fromCurrentContextPath()
	          .path("/files/")
	          .path(String.valueOf(File.getId()))
	          .toUriString();

	      return new ResponseFile(
	          File.getName(),
	          File.getId(),
	          fileDownloadUri,
	          File.getData());
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.OK).body(files);
	  }
	  
	  
	  @PutMapping(value = "/files/{id}", consumes =  {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	  public ResponseEntity<ResponseMessage> updateFile(@PathVariable Integer id, @RequestBody MultipartFile fileData) {
		    String message = "";
		    try {
		      drawerService.update(id, fileData);

		      message = "Updated the file successfully";
		      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		    } catch (Exception e) {
		      message = "Could not update the file!";
		      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		    }
		  }
	  
	  @DeleteMapping("files/{id}") // Map the delete request to the specified path variable
	  public ResponseEntity<ResponseMessage> deleteFile(@PathVariable Integer id) {
	    try {
	      drawerService.deleteFile(id);
	      String message = "File deleted successfully";
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      String message = "Could not delete the file!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }
	
}

