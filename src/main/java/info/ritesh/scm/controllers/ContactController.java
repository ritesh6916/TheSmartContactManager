package info.ritesh.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import info.ritesh.scm.entity.Contact;
import info.ritesh.scm.entity.User;
import info.ritesh.scm.forms.ContactForm;
import info.ritesh.scm.helpers.Helper;
import info.ritesh.scm.helpers.Message;
import info.ritesh.scm.helpers.MessageType;
import info.ritesh.scm.services.ContactService;
import info.ritesh.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

	@Autowired
	private UserService userService;

	@Autowired
	private ContactService contactService;

	Logger logger = LoggerFactory.getLogger(ContactController.class);

	@GetMapping("/add")
	public String addContactView(Model model) {
		ContactForm contactForm = new ContactForm();
		model.addAttribute("contactForm", contactForm);
		return "user/add_contact";
	}

	@PostMapping("/add")
	public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
			Authentication authentication, HttpSession session) {

		System.out.println(contactForm);

		if (result.hasErrors()) {

			result.getAllErrors().forEach(error -> logger.info(error.toString()));
			session.setAttribute("message",
					Message.builder().content("Please correct the following errors").type(MessageType.red).build());
			return "user/add_contact";
		}

		String username = Helper.getEmailOfLoggedInUser(authentication);

		// Form -> Contact
		User user = userService.getUserByEmail(username);
		Contact contact = new Contact();

		contact.setName(contactForm.getName());
		contact.setEmail(contactForm.getEmail());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setFavorite(contactForm.isFavorite());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setWebsiteLink(contactForm.getWebsiteLink());
		contact.setLinkedInLink(contactForm.getLinkedInLink());
		contact.setUser(user);

		// Save Contact
		contactService.save(contact);

		session.setAttribute("message",
				Message.builder().content("You have successfully added a new contact").type(MessageType.green).build());

		return "redirect:/user/contacts/add";

	}

}