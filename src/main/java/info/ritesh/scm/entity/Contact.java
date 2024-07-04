package info.ritesh.scm.entity;

import java.util.ArrayList;

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
	private boolean enabled = false;
	private boolean favorite = false;
	private String websiteLink;
	private String linkedInLink;

	// The Contact is associated with only one User
	@ManyToOne
	private User user;

	// A Contact can have many SocialLinks
	@OneToMany(mappedBy = "contact", fetch = FetchType.EAGER, orphanRemoval = true)
	private ArrayList<SocialLink> socialLinks = new ArrayList<>();

}