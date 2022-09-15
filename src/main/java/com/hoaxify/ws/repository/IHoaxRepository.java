package com.hoaxify.ws.repository;

//import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.hoaxify.ws.entities.Hoax;
import com.hoaxify.ws.entities.User;

public interface IHoaxRepository extends JpaRepository<Hoax, Long>, JpaSpecificationExecutor<Hoax>{

	Page<Hoax> findByUser(Pageable page, User user);
	//Page<Hoax> findByIdLessThanAndUser(long id, User user, Pageable page);
	//Page<Hoax> findByIdLessThan(long id, Pageable page); // Id ye göre bul dedik ama LessThan ile id'den öncekileri getirecek.
	//long countByIdGreaterThan(long id);
	//long countByIdGreaterThanAndUser(long id, User user);
	//List<Hoax> findByIdGreaterThan(long id, Sort sort);
	//List<Hoax> findByIdGreaterThanAndUser(long id, User user, Sort sort);

}
