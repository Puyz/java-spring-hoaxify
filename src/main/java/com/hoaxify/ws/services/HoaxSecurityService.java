package com.hoaxify.ws.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hoaxify.ws.entities.Hoax;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.repository.IHoaxRepository;

@Service
public class HoaxSecurityService {

	@Autowired
	IHoaxRepository hoaxRepository;
	
	public boolean isAllowedToDelete(long id, User currentUser) {
		Optional<Hoax> optionalHoax = hoaxRepository.findById(id);
		if (!optionalHoax.isPresent()) { // Hoax yoksa
			return false;
		}
		Hoax hoax = optionalHoax.get();
		if (hoax.getUser().getId() != currentUser.getId()) {
			return false;
		}
		return true;
	}
}
