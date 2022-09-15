package com.hoaxify.ws.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.hoaxify.ws.dto.HoaxSubmitDto;
import com.hoaxify.ws.entities.Hoax;
import com.hoaxify.ws.entities.User;

public interface IHoaxService {

	void save(HoaxSubmitDto hoaxSubmitDto, User user);
	Page<Hoax> getHoaxes(Pageable page);
	Page<Hoax> getHoaxesByUser(Pageable page, String username);
	Page<Hoax> getOldHoaxes(long id, String username, Pageable page);
	long getNewHoaxesCount(long id, String username);
	List<Hoax> getNewHoaxes(long id, String username, Sort sort);
	void delete(long id);

}
