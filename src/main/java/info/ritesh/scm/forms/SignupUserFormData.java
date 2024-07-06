package info.ritesh.scm.forms;

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

	private String name;
	private String email;
	private String password;
	private String phone;
	private String about;

}
