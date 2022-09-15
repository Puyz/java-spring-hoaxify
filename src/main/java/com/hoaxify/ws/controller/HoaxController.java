package com.hoaxify.ws.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hoaxify.ws.annotations.CurrentUser;
import com.hoaxify.ws.dto.HoaxDto;
import com.hoaxify.ws.dto.HoaxSubmitDto;
import com.hoaxify.ws.entities.Hoax;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.response.GenericResponse;
import com.hoaxify.ws.services.IHoaxService;

@RestController
@RequestMapping("/api/1.0")
public class HoaxController {
	

	@Autowired
	IHoaxService hoaxManager;
	
	GenericResponse genericResponse;
	
	@PostMapping("hoaxes")
	GenericResponse save(@Valid @RequestBody HoaxSubmitDto hoax, @CurrentUser User user) {			
		hoaxManager.save(hoax, user);
		return new GenericResponse("Hoax is saved.");
	}
	
	@GetMapping("hoaxes")
	Page<HoaxDto> getHoaxes(@PageableDefault(sort = "id", direction = Direction.DESC, size = 7) Pageable page){
		return hoaxManager.getHoaxes(page).map(HoaxDto::new);  // HoaxDto::new ile Constructor Referans veriyoruz.
	}
	
	@GetMapping("users/{username}/hoaxes")
	Page<HoaxDto> getHoaxesByUsername(@PathVariable String username, @PageableDefault(sort = "id", direction = Direction.DESC, size = 7) Pageable page){
		return hoaxManager.getHoaxesByUser(page, username).map(HoaxDto::new);
	}
	
	@GetMapping({"hoaxes/{id:[0-9]+}", "users/{username}/hoaxes/{id:[0-9]+}"} ) // id'nin 0 ile 9 arasında sadece rakam içermesini ve + ilede birden fazla olabilir diyoruz yani 1 de yazabiliriz 100000 de
	ResponseEntity<?> getHoaxesRelative(
			@PageableDefault(sort = "id", direction = Direction.DESC, size = 7) Pageable page, 
			@PathVariable long id, 
			@PathVariable(required = false) String username,
			@RequestParam(name = "count", required = false, defaultValue = "false") boolean count, 
			@RequestParam(name = "direction", defaultValue = "before") String direction
			){
		
		if (count) {
			long newHoaxCount = hoaxManager.getNewHoaxesCount(id, username); // Cevap olarak { count: 8 } gibi Json dönecek ve key value şeklinde ve javada bu Map'a karşılık geliyor.
			Map<String, Long> response = new HashMap<>();
			response.put("count", newHoaxCount);
			return ResponseEntity.ok(response);
		}
		
		if (direction.equals("after")) {
			List<Hoax> newHoaxes = hoaxManager.getNewHoaxes(id, username, page.getSort());
			List<HoaxDto> newHoaxesDto = newHoaxes.stream().map(HoaxDto::new).collect(Collectors.toList());
			return ResponseEntity.ok(newHoaxesDto);
		}
		return ResponseEntity.ok(hoaxManager.getOldHoaxes(id, username, page).map(HoaxDto::new));
	}
	
	@DeleteMapping("hoaxes/{id:[0-9]+}")
	@PreAuthorize("@hoaxSecurityService.isAllowedToDelete(#id, principal)") // principal = currentUser ve @hoaxSecurityService bu Service'i çağırırken adını camel casing şeklinde yazmamız gerekiyor. Spring Bean Kuralları.(Spring Bu Beanları Controller, Service, Configuration vs. Kendi içinde Camel Casing şeklinde Tanımlar.)
	// @hoaxSecurityService Eğer uzun gelirse Bu classın içindeki örnek @Service(value="hoaxSecurity") diyerek adını değiştiredebiliriz.
	GenericResponse deleteHoax(@PathVariable long id) {
		hoaxManager.delete(id);
		return new GenericResponse("Hoax silindi.");
	}
}








	
	/*@GetMapping("users/{username}/hoaxes/{id:[0-9]+}")
	ResponseEntity<?> getHoaxesRelativeByUsername(@PathVariable long id, @PathVariable String username, @PageableDefault(sort = "id", direction = Direction.DESC) Pageable page
			, @RequestParam(name = "count", required = false, defaultValue = "false") boolean count, 
			  @RequestParam(name = "direction", defaultValue = "before") String direction){
		if (count) {
			long newHoaxCount = hoaxManager.getNewHoaxesCountByUser(id, username);
			Map<String, Long> response = new HashMap<>();
			response.put("count", newHoaxCount);
			return ResponseEntity.ok(response);
		}
		if (direction.equals("after")) {
			List<Hoax> newHoaxes = hoaxManager.getNewHoaxesByUser(id, username, page.getSort());
			List<HoaxDto> newHoaxesDto = newHoaxes.stream().map(HoaxDto::new).collect(Collectors.toList());
			return ResponseEntity.ok(newHoaxesDto);
		}
		return ResponseEntity.ok(hoaxManager.getOldHoaxesByUser(id, username, page).map(HoaxDto::new));
	}*/
	
	


