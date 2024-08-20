package info.ritesh.scm.controllers;

import java.util.UUID;

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
import info.ritesh.scm.services.ImageService;
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

	@Autowired
	private ImageService imageService;

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

		if (result.hasErrors()) {

			result.getAllErrors().forEach(error -> logger.info(error.toString()));

			session.setAttribute("message",
					Message.builder().content("Please correct the following errors").type(MessageType.red).build());
			return "user/add_contact";
		}

		// 1. get UserName of logged in user
		String username = Helper.getEmailOfLoggedInUser(authentication);

		// form ---> contact entity
		User user = userService.getUserByEmail(username);

		// 2. process the contact data
		Contact contact = new Contact();
		contact.setName(contactForm.getName());
		contact.setFavorite(contactForm.isFavorite());
		contact.setEmail(contactForm.getEmail());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setUser(user);
		contact.setLinkedInLink(contactForm.getLinkedInLink());
		contact.setWebsiteLink(contactForm.getWebsiteLink());

		// 3. set the contact picture URL
		if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
			String filename = UUID.randomUUID().toString();
			String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
			contact.setPicturePath(fileURL);
			contact.setCloudinaryImagePublicId(filename);

		}
		contactService.save(contact);

		// 4. set message to be displayed on the view
		session.setAttribute("message",
				Message.builder().content("You have successfully added a new contact").type(MessageType.green).build());

		return "redirect:/user/contacts/add";

	}

}