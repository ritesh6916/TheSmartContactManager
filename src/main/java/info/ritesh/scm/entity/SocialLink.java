package info.ritesh.scm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SocialLinks")

//generating getters, setters, constructors, and builder using lombok
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialLink {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long linkId;
	private String link;
	private String title;

	// The SocialLink can be associated with only one Contact
	@ManyToOne
	private Contact contact;

}
