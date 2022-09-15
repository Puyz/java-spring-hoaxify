package com.hoaxify.ws.response;

import com.hoaxify.ws.dto.UserDto;

import lombok.Data;

@Data
public class AuthResponse {
	
	private String token;
	
	private UserDto user;
}
