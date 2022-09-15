package com.hoaxify.ws.error;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Client tarafında cevap değerleri Null olmayanları gösterir.
// Mesela AuthController'da validationErrorss u kullanmıyoruz null dönüyor bu sayede response'da gözükmüyor.
public class ApiError {
	private int status;
	
	private String message;
	
	private String path;
	
	private long timestamp = new Date().getTime();
	
	private Map<String, String> validationErrors;
	
	public ApiError(int status, String message, String path) {
		this.status = status;
		this.message = message;
		this.path = path;
	}
	

}
