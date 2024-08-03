package info.ritesh.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// This class is used to store the form data of the user who is signing up.
// Lombok annotations are used to generate the getter and setter methods for the class.
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupUserFormData {

	@NotBlank(message = "Name is required")
	@Size(min = 3, message = "Name should have atleast 3 characters")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid Email")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password should have atleast 6 characters")
	private String password;

	@Size(min = 8, max = 13, message = "Invalid Phone Number")
	private String phone;

	@NotBlank(message = "About is required")
	private String about;

}
