package info.ritesh.scm.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Users")

//generating getters, setters, constructors, and builder using lombok
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class User implements UserDetails {

	@Id
	private String userId;

	@Column(name = "User_name", nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	private String phoneNumber;

	// Lombok will not generate Getter for password
	@Getter(value = lombok.AccessLevel.NONE)
	private String password;

	@Column(length = 1000)
	private String about;

	@Column(length = 500)
	private String profilePicPath;

	@Getter(value = AccessLevel.NONE)
	@Builder.Default
	private boolean enabled = false;

	// Because of this Builder will not ignore the default value
	@Builder.Default
	private boolean emailVerified = false;

	@Builder.Default
	private boolean phoneVerified = false;

	// SELF, GOOGLE, FACEBOOK, GITHUB, LINKEDIN
	@Enumerated(value = EnumType.STRING)
	@Builder.Default
	private Providers provider = Providers.SELF;
	private String providerUserId;

	// A User can have many contacts
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roleList = new ArrayList<>();

	// A User can have many roles {'ROLE_USER', 'ROLE_ADMIN'}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		// Collection of SimpleGrantedAuthority[roles{ADMIN, USER}]
		Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());
		return roles;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		// email is the Username
		return this.email;
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

}
