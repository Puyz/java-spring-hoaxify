package com.hoaxify.ws.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaxify.ws.entities.FileAttachment;
import com.hoaxify.ws.entities.User;

public interface IFileAttachmentRepository extends JpaRepository<FileAttachment, Long>{
	
	List<FileAttachment> findByDateBeforeAndHoaxIsNull(Date date);
	
	List<FileAttachment> findByHoaxUser(User user); // FileAttachment içindeki  Hoax'ın içindeki User'a göre bul.
}
