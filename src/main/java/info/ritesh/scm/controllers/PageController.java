package info.ritesh.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

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
	public String getRegisterPage() {
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

}
