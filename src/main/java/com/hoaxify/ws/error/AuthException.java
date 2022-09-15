package com.hoaxify.ws.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthException extends RuntimeException	{

	private static final long serialVersionUID = 1L;
	
	private String message = "{hoaxify.constraint.Auth.message}"; // {hoaxify.constraint.Auth.message} properties içine yazdım fakat String olarak algıladı çalışmadı sonra bak.

	
}
