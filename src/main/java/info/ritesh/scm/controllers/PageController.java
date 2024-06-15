package info.ritesh.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public String requestMethodName(Model m) {
		System.out.println("Home page Handler");
		m.addAttribute("name", "Ritesh");
		m.addAttribute("git","https://github.com/ritesh6916");
		return "home";
	}

}
