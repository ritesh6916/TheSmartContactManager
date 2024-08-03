package info.ritesh.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	// User Dashboard Page
	@GetMapping("/dashboard")
	public String showDashboard() {
		return "user/dashboard";
	}

	// User Profile Page
	@GetMapping("/profile")
	public String showUserProfile() {
		return "user/profile";
	}

	// User Profile Page

	// User Add-Contact Page

	// User view contacts

	// User edit Contacts Page

	// User delete Contacts Page

	// User search Contact Page

}
