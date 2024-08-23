package info.ritesh.scm.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Contacts")

//generating getters, setters, constructors, and builder using lombok
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Contact {

	@Id
	private String contactId;
	private String name;
	private String email;
	private String phoneNumber;
	private String address;
	private String picturePath;

	@Column(length = 1000)
	private String description;

	@Builder.Default
	private boolean enabled = false;

	@Builder.Default
	private boolean favorite = false;
	private String websiteLink;
	private String linkedInLink;

	private String cloudinaryImagePublicId;

	// The Contact is associated with only one User
	@JsonIgnore
	@ManyToOne
	private User user;

	// A Contact can have many SocialLinks
	@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<SocialLink> socialLinks = new ArrayList<>();

}