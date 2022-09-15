	package com.hoaxify.ws.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hoaxify.ws.entryPoint.AuthEntryPoint;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration{
	
	@Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		
		http.headers().frameOptions().disable(); // Security'den dolayı h2-console'da (Frame) 'localhost bağlanmayı reddetti' sorunu için ekledim.
			
		http.exceptionHandling().authenticationEntryPoint(new AuthEntryPoint());
		
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.PUT,"/api/1.0/users/{username}").authenticated()
				.antMatchers(HttpMethod.POST,"/api/1.0/hoaxes").authenticated()
				.antMatchers(HttpMethod.POST,"/api/1.0/hoax-attachments").authenticated()
				.antMatchers(HttpMethod.POST,"/api/1.0/logout").authenticated()
			.and()
			.authorizeRequests().anyRequest().permitAll();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); 
		// Bir kere 200 OK yani authorized yapınca cookiede kalıyor ve sonra unAuth request atınca hala OK cevabı veriyor. 
		//bunun olmaması için stateless yani her istekte auth yapmamız lazım
        
		
		http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
    }
	
	@Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	@Bean
	TokenFilter tokenFilter() {
		return new TokenFilter();
	}
}
