package com.hoaxify.ws.dto;


import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class HoaxSubmitDto {
	
	@Size(min = 1, max = 1000)
	private String content;
	
	private long attachmentId;
}
