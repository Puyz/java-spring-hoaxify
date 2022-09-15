package com.hoaxify.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.hoaxify.ws.entities.FileAttachment;
import com.hoaxify.ws.services.FileService;

@RestController
@RequestMapping("/api/1.0")
public class FileController {
	
	@Autowired
	FileService fileService;
	
	@PostMapping("hoax-attachments")
	FileAttachment saveHoaxAttachment(MultipartFile file) {
		return fileService.saveHoaxAttachment(file);
	}
}
