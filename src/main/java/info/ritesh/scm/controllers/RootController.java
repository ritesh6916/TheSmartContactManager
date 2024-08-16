package info.ritesh.scm.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import info.ritesh.scm.entity.User;
import info.ritesh.scm.helpers.Helper;
import info.ritesh.scm.services.UserService;

// methods of RootController will be triggered for each and every request

@ControllerAdvice
public class RootController {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@ModelAttribute
	public void addLoggedInUserInformation(Model model, Authentication authentication) {

		if (authentication == null) {
			return;
		}

		String username = Helper.getEmailOfLoggedInUser(authentication);
		logger.info("User logged in: {}", username);

		// Fetch user details from database
		User user = userService.getUserByEmail(username);

		System.out.println("Adding logged in user information to the model for " + username);
		model.addAttribute("loggedInUser", user);
	}

}
