package com.bpmn.drawer.DrawerService;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.bpmn.drawer.entity.File;

public interface DrawerService {
	File store(MultipartFile file) throws IOException;
	File getFile(Integer id);
	Stream<File> getAllFiles();
	void update(Integer id, MultipartFile fileData) throws IOException;
	void deleteFile(Integer id);
}
