package com.hoaxify.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import com.hoaxify.ws.dto.HoaxSubmitDto;
import com.hoaxify.ws.entities.User;
import com.hoaxify.ws.services.IHoaxService;
import com.hoaxify.ws.services.IUserService;*/

@SpringBootApplication // exclude ile istediğimiz classları devre dışı bırakabiliriz.
public class WsApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(WsApplication.class, args);
	}
	

	
	/*@Bean
	@Profile("dev") // sadece Profile dev olduğunda çalışsın demiş olduk.
	CommandLineRunner createInitialUsers(IUserService userManager, IHoaxService hoaxManager) {
		return new CommandLineRunner() {
			
			@Override
			public void run(String... args) throws Exception {
				for (int i = 1; i <= 22; i++) {
					User user = new User();
					user.setUsername("user"+i);
					user.setPassword("123");
					user.setDisplayName("display"+i);
					userManager.addUser(user);		
					for(int j = 1; j<=2; j++) {
						HoaxSubmitDto hoax = new HoaxSubmitDto();
						hoax.setContent("Hoax: "+j+ " from user: "+i);
						hoaxManager.save(hoax, user);
					}
				}
			}
		};
	}*/

}
