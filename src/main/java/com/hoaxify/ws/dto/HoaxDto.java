package com.hoaxify.ws.dto;

import com.hoaxify.ws.entities.Hoax;
import lombok.Data;

@Data
public class HoaxDto {

	private Long id;
	
	private String content;
		
	private Long timestamp;
	
	private UserDto user;
	
	private FileAttachmentDto fileAttachment;
	
	public HoaxDto(Hoax hoax) {
		this.setId(hoax.getId());
		this.setContent(hoax.getContent());
		this.setTimestamp(hoax.getTimestamp().getTime());
		this.setUser(new UserDto(hoax.getUser()));
		if (hoax.getFileAttachment() != null) {			
			this.setFileAttachment(new FileAttachmentDto(hoax.getFileAttachment()));
		}
	}
	
	


}
