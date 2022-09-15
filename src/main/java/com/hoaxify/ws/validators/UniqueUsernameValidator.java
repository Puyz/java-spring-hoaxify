package com.hoaxify.ws.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import com.hoaxify.ws.annotations.UniqueUsername;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.repository.IUserRepository;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{
	
	@Autowired
	IUserRepository userRepository;

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		User user = userRepository.findByUsername(username);
		return (user == null) ? true : false;
	}

}
