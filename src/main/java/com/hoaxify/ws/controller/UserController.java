package com.hoaxify.ws.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hoaxify.ws.annotations.CurrentUser;
import com.hoaxify.ws.dto.UserDto;
import com.hoaxify.ws.dto.UserUpdateDto;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.response.GenericResponse;
import com.hoaxify.ws.services.IUserService;


@RestController
@RequestMapping("/api/1.0")
public class UserController{
	
	@Autowired
	IUserService userManager;
	
	GenericResponse genericResponse;
	

	/* Bu port 8080 kullanıyor react'de 3000 kullanıyoruz farklı port olunca frontendde istek atınca Cross hata veriyor bunun için. (React'da package.json ile proxy ayarı yaparsak buna gerek kalmıyor)
	@CrossOrigin  */
	// @ResponseStatus(HttpStatus.CREATED) -> Bu annotation ile response Cevabını değiştirebiliriz.
	
	@PostMapping("users")
	public GenericResponse createUser(@Valid @RequestBody User user) {			
		userManager.addUser(user);
		return new GenericResponse("Kullanıcı oluşturuldu.");
	}
	
	
	@GetMapping("users")
	public Page<UserDto> getUsers(Pageable page, @CurrentUser User user){
		return userManager.getUsers(page, user).map(UserDto::new);
	}
	
	@GetMapping("users/{username}")
	public UserDto getUser(@PathVariable String username) {
		User user =	userManager.getUserByUsername(username);
		return new UserDto(user);
		
	}
	
	@PutMapping("users/{username}")
	@PreAuthorize("#username == principal.username") // # ile Methodun parametre değerlerini alabiliyoruz. ve burada koşul yazıyoruz sağlanırsa method çalışacak. principal özel keyword CurrentUserda kullanılan obje
	public UserDto updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable String username) {
		User user = userManager.updateUser(username, userUpdateDto);
		return new UserDto(user);
	}
	
	@DeleteMapping("users/{username}")
	@PreAuthorize("#username == principal.username")
	GenericResponse deleteUser(@PathVariable String username) {
		userManager.deleteUser(username);
		return new GenericResponse("Kullanıcı silindi");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		// Bu UserController Classın içinde bu exception yakalanırsa bu method çalışır.
		// ErrorHandler'da ortak çözüm ile hallettik.
	
		/*@ExceptionHandler(MethodArgumentNotValidException.class)
		@ResponseStatus(HttpStatus.BAD_REQUEST)
		public ApiError handleValidationException(MethodArgumentNotValidException exception) {
			ApiError apiError = new ApiError(400, "Doğrulama hatası", "/api/1.0/users");
			Map<String, String> validationErrors = new HashMap<>();
			for(FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
				validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			apiError.setValidationErrors(validationErrors);
			return apiError;
		}
		*/
	
	
	
	
	
	
	
	
}
