package com.bpmn.drawer.DrawerService;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.bpmn.drawer.DrawerDAO.DrawerDAO;
import com.bpmn.drawer.entity.File;

@Service
public class DrawerServiceIMPL implements DrawerService {
	  @Autowired
	  private DrawerDAO drawerDAO;

	  public File store(MultipartFile file) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    File File = new File(0, fileName, file.getBytes());

	    return drawerDAO.save(File);
	  }

	  public File getFile(Integer id) {
	    return drawerDAO.findById(id).get();
	  }
	  public Stream<File> getAllFiles() {
		    return drawerDAO.findAll().stream();
		  }
	  
	  
	  public void update(Integer id, MultipartFile fileData) throws IOException {
		    File file = drawerDAO.findById(id).orElse(null);
		    if (file != null) {
		    	System.out.println(file.getId());
		    	System.out.println(file.getName());
		    	System.out.println(file.getData());
		    	System.out.println(fileData.getBytes());
		      file.setData(fileData.getBytes());
		      drawerDAO.save(file);
		    }

	  }

	  public void deleteFile(Integer id) {
	        drawerDAO.deleteById(id);
	    }

}
