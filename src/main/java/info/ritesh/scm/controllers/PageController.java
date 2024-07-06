package info.ritesh.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import info.ritesh.scm.entity.Providers;
import info.ritesh.scm.entity.User;
import info.ritesh.scm.forms.SignupUserFormData;
import info.ritesh.scm.services.impl.UserServiceImpl;

@Controller
public class PageController {

	@Autowired
	private UserServiceImpl userServiceImpl;

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
		model.addAttribute("defaultuserData", signupUserFormData);
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
	public String registerUser(@ModelAttribute SignupUserFormData signupUserFormData) {

		// validate the data

		// SignupUserFormData -> User

		User user = User.builder().name(signupUserFormData.getName()).email(signupUserFormData.getEmail())
				.password(signupUserFormData.getPassword()).phoneNumber(signupUserFormData.getPhone())
				.about(signupUserFormData.getAbout()).profilePicPath("src/main/resources/static/images/telephone.png")
				.emailVerified(false).phoneVerified(false).enabled(false).provider(Providers.SELF)
				.providerUserId("self").build();
		System.out.println(signupUserFormData.getPassword());

		// save to database
		userServiceImpl.savUser(user);

		return "redirect:/register";
	}
}
