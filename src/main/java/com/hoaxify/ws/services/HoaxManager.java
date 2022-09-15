package com.hoaxify.ws.services;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hoaxify.ws.dto.HoaxSubmitDto;
import com.hoaxify.ws.entities.FileAttachment;
import com.hoaxify.ws.entities.Hoax;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.repository.IFileAttachmentRepository;
import com.hoaxify.ws.repository.IHoaxRepository;

@Service
public class HoaxManager implements IHoaxService{
	
	IHoaxRepository hoaxRepository;
	IUserService userManager;
	IFileAttachmentRepository fileAttachmentRepository;
	FileService fileService;
	
	public HoaxManager(IHoaxRepository hoaxRepository, IFileAttachmentRepository fileAttachmentRepository, FileService fileService, IUserService userManager) {
		this.hoaxRepository = hoaxRepository;
		this.fileAttachmentRepository = fileAttachmentRepository;
		this.fileService = fileService;
		this.userManager = userManager;
	}


	@Override
	@Transactional
	public void save(HoaxSubmitDto hoaxSubmitDto, User user) {
		Hoax hoax = new Hoax();
		hoax.setContent(hoaxSubmitDto.getContent());
		hoax.setUser(user);
		hoaxRepository.save(hoax);
		Optional<FileAttachment> optionalFileAttachment = fileAttachmentRepository.findById(hoaxSubmitDto.getAttachmentId());
		if (optionalFileAttachment.isPresent()) {
				FileAttachment fileAttachment = optionalFileAttachment.get();
				fileAttachment.setHoax(hoax);
				fileAttachmentRepository.save(fileAttachment);
		}
	}

	@Override
	@Transactional
	public Page<Hoax> getHoaxes(Pageable page) {
		return hoaxRepository.findAll(page);
	}

	@Override
	public Page<Hoax> getHoaxesByUser(Pageable page, String username) {
		User user = userManager.getUserByUsername(username);
		return hoaxRepository.findByUser(page, user);
	}

	@Override
	public Page<Hoax> getOldHoaxes(long id, String username, Pageable page) {
		Specification<Hoax> specification = idLessThan(id);
		if (username != null) {
			User user = userManager.getUserByUsername(username);
			specification = specification.and(userIs(user));
			//return hoaxRepository.findByIdLessThanAndUser(id, user, page);
		}
		return hoaxRepository.findAll(specification, page);
		//return hoaxRepository.findByIdLessThan(id, page);
	}

	@Override
	public long getNewHoaxesCount(long id, String username) {
		Specification<Hoax> specification = idGreaterThan(id);
		if (username != null) {
			User user = userManager.getUserByUsername(username);
			specification = specification.and(userIs(user));
			//return hoaxRepository.countByIdGreaterThanAndUser(id, user);
		}
		return hoaxRepository.count(specification);
		//return hoaxRepository.countByIdGreaterThan(id);
	}

	@Override
	public List<Hoax> getNewHoaxes(long id, String username, Sort sort) {
		Specification<Hoax> specification = idGreaterThan(id);
		if (username != null) {
			User user = userManager.getUserByUsername(username);
			specification = specification.and(userIs(user));
			//return hoaxRepository.findByIdGreaterThanAndUser(id, user, sort);
		}
		return hoaxRepository.findAll(specification, sort);
		//return hoaxRepository.findByIdGreaterThan(id, sort);
	}
	
	@Override
	public void delete(long id) {
		Hoax hoax = hoaxRepository.getReferenceById(id);
		if (hoax.getFileAttachment() != null) {
			String fileName = hoax.getFileAttachment().getName();
			fileService.deleteAttachmentFile(fileName);
		}
		hoaxRepository.deleteById(id);
	}
	
	Specification<Hoax> idLessThan(long id){
		return (root, query, critirialBuilder) -> {
			return critirialBuilder.lessThan(root.get("id"), id);
		};
	}
	Specification<Hoax> idGreaterThan(long id){
		return (root, query, critirialBuilder) -> {
			return critirialBuilder.greaterThan(root.get("id"), id);
		};
	}
	Specification<Hoax> userIs(User user){
		return (root, query, critirialBuilder) -> {
			return critirialBuilder.equal(root.get("user"), user);
		};
	}

	

	
	

}
