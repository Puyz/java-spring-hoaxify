package com.hoaxify.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hoaxify.ws.dto.Credentials;
import com.hoaxify.ws.response.AuthResponse;
import com.hoaxify.ws.response.GenericResponse;
import com.hoaxify.ws.services.AuthService;

@RestController
@RequestMapping("/api/1.0")
public class AuthController{
	
	@Autowired
	AuthService authService;
	
	@PostMapping("auth")
	AuthResponse handleAuthentication(@RequestBody Credentials credentails) {
		return authService.authenticate	(credentails);
		
	}
	
	@PostMapping("logout")
	GenericResponse handleLogout(@RequestHeader(name = "Authorization") String authorization) {
		String token = authorization.substring(7);
		authService.clearToken(token);
		return new GenericResponse("Logout success");
		
	}

}
