package info.ritesh.scm.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")

//generating getters, setters, constructors, and builder using lombok
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {

	@Id
	private String userId;

	@Column(name = "User_name", nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	private String phoneNumber;
	private String password;

	@Column(length = 1000)
	private String about;

	@Column(length = 500)
	private String profilePicPath;

	private boolean enabled = false;
	private boolean emailVerified = false;
	private boolean phoneVerified = false;

	// SELF, GOOGLE, FACEBOOK, GITHUB, LINKEDIN
	@Enumerated(value = EnumType.STRING)
	private Providers provider = Providers.SELF;
	private String providerUserId;

	// A User can have many contacts
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();

}
