package com.hoaxify.ws.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hoaxify.ws.entities.User;

public interface IUserRepository extends JpaRepository<User, Integer>{
	
	User findByUsername(String username);
	
	Page<User> findByUsernameNot(String username, Pageable page);
	
}
