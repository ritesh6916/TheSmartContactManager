package info.ritesh.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import info.ritesh.scm.entity.User;
import info.ritesh.scm.helpers.Message;
import info.ritesh.scm.helpers.MessageType;
import info.ritesh.scm.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

	// verify email

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/verify-email")
	public String verifyEmail(@RequestParam("token") String token, HttpSession session) {

		System.out.println("Email verification token : " + token);

		User user = userRepo.findByEmailToken(token).orElse(null);

		if (user != null) {

			if (user.getEmailToken().equals(token) & user.isEmailVerified()) {
				session.setAttribute("message", Message.builder().type(MessageType.green)
						.content("You are already verified: Please Login").build());

				return "redirect:/login";
			} else if (user.getEmailToken().equals(token)) {
				user.setEmailVerified(true);
				user.setEnabled(true);
				userRepo.save(user);
				session.setAttribute("message", Message.builder().type(MessageType.green)
						.content("Congratulations!!   You are verified now, Please proceed to login").build());
				return "success_page";
			}

			session.setAttribute("message", Message.builder().type(MessageType.red)
					.content("Email not verified ! Token is not associated with user .").build());
			return "error_page";

		}

		session.setAttribute("message", Message.builder().type(MessageType.red)
				.content("Email not verified ! Token is not associated with user .").build());

		return "error_page";
	}

}