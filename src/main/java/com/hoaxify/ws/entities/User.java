package com.hoaxify.ws.entities;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import com.hoaxify.ws.annotations.UniqueUsername;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "USERS")
@NoArgsConstructor
public class User implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3795786898494121793L;

	public User(String username, String displayName, String password) {
		this.username = username;
		this.displayName = displayName;
		this.password = password;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@NotNull(message = "{hoaxify.constraint.username.NotNull.message}")
	@Size(min = 4, max = 255)
	@UniqueUsername
	@Column(name = "USER_NAME")
	private String username;
	
	
	@NotNull(message = "{hoaxify.constraint.displayName.NotNull.message}")
	@Size(min = 4, max = 255)
	@Column(name = "DISPLAY_NAME")
	private String displayName;
	
	
	@NotNull(message = "{hoaxify.constraint.password.NotNull.message}")
	@Pattern(
			regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", // Pattern
			message = "{hoaxify.constraint.password.Pattern.message}"
	)
	@Size(min = 8, max = 255)
	@Column(name = "PASSWORD")
	private String password;
	
	//@Lob // Large Object -> Değer uzunluğu fazla olduğu için Lob kullanıyoruz.
	@Column(name = "IMAGE")
	private String image;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Hoax> hoax;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private List<Token> token;
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("Role_user");
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	

	
	

}
