package info.ritesh.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import info.ritesh.scm.entity.Providers;
import info.ritesh.scm.entity.User;
import info.ritesh.scm.forms.SignupUserFormData;
import info.ritesh.scm.helpers.Message;
import info.ritesh.scm.helpers.MessageType;
import info.ritesh.scm.services.impl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@GetMapping("/")
	public String showHome() {
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String requestMethodName(Model m) {

		m.addAttribute("name", "Ritesh");
		m.addAttribute("git", "https://github.com/ritesh6916");
		return "home";
	}

	@GetMapping("/about")
	public String showAbout() {
		return "about";
	}

	@GetMapping("/register")
	public String getRegisterPage(Model model) {

		// create an object of SignupUserForm & send it to the SignUp page
		SignupUserFormData signupUserFormData = new SignupUserFormData();
		/*
		 * signupUserFormData.setName("Ritesh Singh");
		 * signupUserFormData.setEmail("abcd@xyz.com");
		 * signupUserFormData.setPhone("1234567890");
		 * signupUserFormData.setAbout("I am a software developer");
		 */
		model.addAttribute("signupUserFormData", signupUserFormData);
		return "register";
	}

	@GetMapping("/login")
	public String getLoginpPage() {
		return "login";
	}

	@GetMapping("/contact")
	public String getContactpPage() {
		return "contact";
	}

	// process register form
	@PostMapping("/do-register")
	public String registerUser(@Valid @ModelAttribute SignupUserFormData signupUserFormData,
			BindingResult rBindingResult, HttpSession session) {

		// validate the data
		if (rBindingResult.hasErrors()) {

			/*
			 * Message Successmessage =
			 * Message.builder().content("Registration UnSuccessful ").type(MessageType.red)
			 * .build();
			 * 
			 * // alert message session.setAttribute("message", Successmessage);
			 */

			return "register";
		}

		// SignupUserFormData -> User

		/*
		 * User user =
		 * User.builder().name(signupUserFormData.getName()).email(signupUserFormData.
		 * getEmail())
		 * .password(signupUserFormData.getPassword()).phoneNumber(signupUserFormData.
		 * getPhone()) .about(signupUserFormData.getAbout()).profilePicPath(
		 * "src/main/resources/static/images/telephone.png")
		 * .emailVerified(false).phoneVerified(false).enabled(false).provider(Providers.
		 * SELF) .providerUserId("self").build();
		 * System.out.println(signupUserFormData.getPassword());
		 */

		User user = new User();
		user.setName(signupUserFormData.getName());
		user.setEmail(signupUserFormData.getEmail());
		user.setPassword(signupUserFormData.getPassword());
		user.setPhoneNumber(signupUserFormData.getPhone());
		user.setAbout(signupUserFormData.getAbout());
		user.setProfilePicPath("src/main/resources/static/images/telephone.png");
		user.setEmailVerified(false);
		user.setPhoneVerified(false);
		user.setEnabled(false);
		user.setProvider(Providers.SELF);
		user.setProviderUserId(null);

		// save to database
		userServiceImpl.savUser(user);

		Message Successmessage = Message.builder().content("Registration Successful !!").type(MessageType.green)
				.build();

		// alert message
		session.setAttribute("message", Successmessage);

		return "redirect:/register";
	}
}
