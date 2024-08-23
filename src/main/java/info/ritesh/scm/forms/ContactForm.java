package info.ritesh.scm.forms;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import info.ritesh.scm.validators.ValidFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid Email Address [ example@gmail.com ]")
	private String email;

	@NotBlank(message = "Phone Number is required")
	@Pattern(regexp = "^[0-9]*$", message = "Invalid Phone Number")
	@Length(min = 10, max = 11, message = "Phone Number must be 10 digits or 11 with 0 prefix")
	private String phoneNumber;

	@NotBlank(message = "Address is required")
	private String address;

	private String description;

	private boolean favorite;

	private String websiteLink;

	private String linkedInLink;

	@ValidFile(message = "Invalid File")
	private MultipartFile contactImage;

	private String picture;

}
