package com.hoaxify.ws.services;

import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hoaxify.ws.dto.Credentials;
import com.hoaxify.ws.dto.UserDto;
import com.hoaxify.ws.entities.Token;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.error.AuthException;
import com.hoaxify.ws.repository.ITokenRepository;
import com.hoaxify.ws.repository.IUserRepository;
import com.hoaxify.ws.response.AuthResponse;

@Service
public class AuthService {
	
	IUserRepository userRepository;
	PasswordEncoder passwordEncoder;
	ITokenRepository tokenRepository;
		
	public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder, ITokenRepository tokenRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenRepository = tokenRepository;
	}



	public AuthResponse authenticate(Credentials credentails) {
		User inDB = userRepository.findByUsername(credentails.getUsername());
		if (inDB == null) { // Kullanıcı yok ise
			throw new AuthException();
		}
		
		boolean matches = passwordEncoder.matches(credentails.getPassword(), inDB.getPassword()); // Şifre doğrulaması
		
		if (!matches) {
			throw new AuthException();
		}
		// Kontrol sağlandı şimdi token üretmemiz lazım.
		UserDto userDto = new UserDto(inDB);
		String token = generateRandomToken();
		
		Token tokenEntity = new Token();
		tokenEntity.setToken(token);
		tokenEntity.setUser(inDB);
		tokenRepository.save(tokenEntity);
		
		// Token ürettik şimdi client'a (frontend) user ile tokenı dönelim.
		AuthResponse response = new AuthResponse();
		response.setUser(userDto);
		response.setToken(token);
		return response;
		
	}


	@Transactional
	public UserDetails getUserDetails(String token) {
		Optional<Token> optionalToken = tokenRepository.findById(token);
		if (!optionalToken.isPresent()) {			
			return null;
		}
		return optionalToken.get().getUser();
	}
	
	private String generateRandomToken() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public void clearToken(String token) {
		tokenRepository.deleteById(token);
	}
}
