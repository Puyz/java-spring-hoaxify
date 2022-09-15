package com.hoaxify.ws.services;

import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hoaxify.ws.dto.UserUpdateDto;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.error.NotFoundException;
import com.hoaxify.ws.repository.IUserRepository;


@Service
public class UserManager implements IUserService{
	
	IUserRepository userRepository;
	PasswordEncoder passwordEncoder; // PasswordEncoder bir interfacedir. Constructor ile new BCryptPasswordEncoder yapacağız.
	FileService fileService;
	
	public UserManager(IUserRepository userRepository, PasswordEncoder passwordEncoder, FileService fileService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.fileService = fileService;
	}

	@Override
	@Transactional
	public Page<User> getUsers(Pageable page, User user) {
		if (user != null) {
			return userRepository.findByUsernameNot(user.getUsername(), page);
		}
		return userRepository.findAll(page);
	}

	@Override
	@Transactional
	public User getUserById(int id) {
		return userRepository.findById(id).get();
	}
	
	@Override
	@Transactional
	public User getUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new NotFoundException();
		}
		return user;
	}
	
	@Override
	@Transactional
	public void addUser(User user) {
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void deleteUser(String username) {
		User inDB = getUserByUsername(username);
		fileService.deleteAllStoredFileForUser(inDB);
		userRepository.delete(inDB);
	}

	@Override
	@Transactional
	public User updateUser(String username, UserUpdateDto userUpdateDto) {
		User user = getUserByUsername(username);
		user.setDisplayName(userUpdateDto.getDisplayName());
		if (userUpdateDto.getImage() != null) {
			String oldImageName = user.getImage();
			try {
				String storedFileName = fileService.writeBase64EncodedStringToFile(userUpdateDto.getImage());
				user.setImage(storedFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileService.deleteProfileImage(oldImageName);
		}
		return userRepository.save(user);
	}



}
