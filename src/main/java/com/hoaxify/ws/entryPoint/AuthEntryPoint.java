package com.hoaxify.ws.entryPoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class AuthEntryPoint implements AuthenticationEntryPoint{

	// bu new AuthenticationEntryPoint(), headers içinde www- ile başlayan authorizate yüzünden pop-up şeklinde tarayıcıda username password ile security i doğrulama istiyor 
	// bu override ile içinden o pop-up u istemedğimiz için sadece sendError metodunu alıyoruz.
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
		
	}

}
