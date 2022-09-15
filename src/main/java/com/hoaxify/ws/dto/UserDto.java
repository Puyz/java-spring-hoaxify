package com.hoaxify.ws.dto;

import com.hoaxify.ws.entities.User;

import lombok.Data;

@Data
public class UserDto {
	
	private String username;
	
	private String displayName;
	
	private String image;
	
	public UserDto(User user) {
		this.setUsername(user.getUsername());
		this.setDisplayName(user.getDisplayName());
		this.setImage(user.getImage());
	}
	
}
