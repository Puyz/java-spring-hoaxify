package com.hoaxify.ws.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.tika.Tika;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.hoaxify.ws.configuration.AppConfiguration;
import com.hoaxify.ws.entities.FileAttachment;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.repository.IFileAttachmentRepository;

@Service
@EnableScheduling
public class FileService {
	
	IFileAttachmentRepository fileAttachmentRepository;
	AppConfiguration appConfiguration;
	Tika tika;
	
	
	public FileService(AppConfiguration appConfiguration, IFileAttachmentRepository fileAttachmentRepository) {
		this.appConfiguration = appConfiguration;
		this.tika = new Tika();
		this.fileAttachmentRepository = fileAttachmentRepository;
	}

	public String writeBase64EncodedStringToFile(String image) throws IOException{	
		String fileName = generateRandomName();
		File target = new File(appConfiguration.getProfileStoragePath()+"/"+fileName);
		OutputStream outputStream = new FileOutputStream(target);
		
		byte[] base64Encoded = Base64.getDecoder().decode(image);
	
		outputStream.write(base64Encoded);	
		outputStream.close();
		return fileName;
	}
	
	public String generateRandomName() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public void deleteProfileImage(String oldImageName) {
		if (oldImageName == null) {
			return;
		}
		deleteFile(Paths.get(appConfiguration.getProfileStoragePath(), oldImageName));
	}
	
	public void deleteAttachmentFile(String file) {
		if (file == null) {
			return;
		}
		deleteFile(Paths.get(appConfiguration.getAttachmentStoragePath(), file));
	}
	
	private void deleteFile(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String detectType(String value) {
		byte[] base64Encoded = Base64.getDecoder().decode(value);
		return tika.detect(base64Encoded);
	}
	public String detectType(byte[] byteArray) {		
		return tika.detect(byteArray);
	}

	public FileAttachment saveHoaxAttachment(MultipartFile multipartFile) {
		String fileName = generateRandomName();
		File target = new File(appConfiguration.getAttachmentStoragePath()+"/"+fileName);
		String fileType = null;
		try {
			OutputStream outputStream = new FileOutputStream(target);			
			outputStream.write(multipartFile.getBytes());	
			outputStream.close();
			fileType = detectType(multipartFile.getBytes());
		} catch (Exception e) {}
		//////////// Dosyaya kayıt ettik şimdi DB ye kayıt edelim.
		FileAttachment fileAttachment = new FileAttachment();
		fileAttachment.setName(fileName);
		fileAttachment.setFileType(fileType);
		return fileAttachmentRepository.save(fileAttachment);
	}
	
	@Scheduled(fixedRate = 24*60*60*1000)
	public void cleanupStorage() {
		Date twentyFourHourAge = new Date(System.currentTimeMillis() - (24*60*60*1000));
		List<FileAttachment> filesToBeDeleted = fileAttachmentRepository.findByDateBeforeAndHoaxIsNull(twentyFourHourAge);
		for(FileAttachment file : filesToBeDeleted) {
			deleteAttachmentFile(file.getName());
			fileAttachmentRepository.deleteById(file.getId());
		}
	}

	public void deleteAllStoredFileForUser(User inDB) {
		deleteProfileImage(inDB.getImage());
		List<FileAttachment> filesToBeRemoved = fileAttachmentRepository.findByHoaxUser(inDB);
		for(FileAttachment file : filesToBeRemoved) {
			deleteAttachmentFile(file.getName());
		}
		
	}

}
