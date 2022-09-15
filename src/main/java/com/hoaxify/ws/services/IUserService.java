package com.hoaxify.ws.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hoaxify.ws.dto.UserUpdateDto;
import com.hoaxify.ws.entities.*;

public interface IUserService {
	
	Page<User> getUsers(Pageable page, User user);
	User getUserById(int id);
	User getUserByUsername(String username);
	void addUser(User user);
	void deleteUser(String username);
	User updateUser(String username, UserUpdateDto userUpdateDto); 
	

}
