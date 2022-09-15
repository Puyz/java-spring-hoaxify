package com.hoaxify.ws.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.hoaxify.ws.annotations.FileType;

import lombok.Data;

@Data
public class UserUpdateDto {

	@NotNull(message = "{hoaxify.constraint.displayName.NotNull.message}")
	@Size(min = 4, max = 255)
	private String displayName;
	
	@FileType(types = {"jpeg","png"})
	private String image;
}
