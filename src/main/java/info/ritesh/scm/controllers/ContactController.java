package info.ritesh.scm.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import info.ritesh.scm.entity.Contact;
import info.ritesh.scm.entity.User;
import info.ritesh.scm.forms.ContactForm;
import info.ritesh.scm.forms.ContactSearchForm;
import info.ritesh.scm.helpers.AppConstants;
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

	// To display the add-contact form
	@GetMapping("/add")
	public String addContactView(Model model) {
		ContactForm contactForm = new ContactForm();
		model.addAttribute("contactForm", contactForm);
		return "user/add_contact";
	}

	// Save new contact to Cloudinary (Cloud) and database
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

	// To display the list of contacts of logged in user
	// The URL is /user/contacts
	@GetMapping
	public String viewContacts(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
			@RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
			@RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
			Authentication authentication) {

		// load all the user contacts
		String username = Helper.getEmailOfLoggedInUser(authentication);

		User user = userService.getUserByEmail(username);

		Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

		model.addAttribute("pageContact", pageContact);
		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

		model.addAttribute("contactSearchForm", new ContactSearchForm());

		return "user/contacts";
	}

	// search handler

	@GetMapping("/search")
	public String searchHandler(

			@ModelAttribute ContactSearchForm contactSearchForm,
			@RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
			@RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
			Authentication authentication) {

		logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

		var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

		Page<Contact> pageContact = null;
		if (contactSearchForm.getField().equalsIgnoreCase("name")) {
			pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
					user);
		} else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
			pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
					user);
		} else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
			pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
					direction, user);
		}

		logger.info("pageContact {}", pageContact);

		model.addAttribute("contactSearchForm", contactSearchForm);

		model.addAttribute("pageContact", pageContact);

		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

		return "user/search";
	}

	// Detete contact
	@GetMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable("contactId") String contactId, HttpSession session) {

		contactService.delete(contactId);
		logger.info("contactId {} deleted", contactId);

		session.setAttribute("message",
				Message.builder().content("Contact is Deleted successfully !! ").type(MessageType.green).build()

		);

		return "redirect:/user/contacts";
	}

	@GetMapping("/view/{contactId}")
	public String updateContactFormView(@PathVariable("contactId") String contactId, Model model) {

		var contact = contactService.getById(contactId);
		ContactForm contactForm = new ContactForm();
		contactForm.setName(contact.getName());
		contactForm.setEmail(contact.getEmail());
		contactForm.setPhoneNumber(contact.getPhoneNumber());
		contactForm.setAddress(contact.getAddress());
		contactForm.setDescription(contact.getDescription());
		contactForm.setFavorite(contact.isFavorite());
		contactForm.setWebsiteLink(contact.getWebsiteLink());
		contactForm.setLinkedInLink(contact.getLinkedInLink());
		contactForm.setPicture(contact.getPicturePath());
		;
		model.addAttribute("contactForm", contactForm);
		model.addAttribute("contactId", contactId);

		return "user/update_contact_view";
	}

	// Performing actual update of the contact
	@PostMapping(value = "/update/{contactId}")
	public String updateContact(@PathVariable("contactId") String contactId,
			@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Model model) {

		// return same page in case of errors
		if (bindingResult.hasErrors()) {
			return "user/update_contact_view";
		}

		var con = contactService.getById(contactId);
		con.setContactId(contactId);
		con.setName(contactForm.getName());
		con.setEmail(contactForm.getEmail());
		con.setPhoneNumber(contactForm.getPhoneNumber());
		con.setAddress(contactForm.getAddress());
		con.setDescription(contactForm.getDescription());
		con.setFavorite(contactForm.isFavorite());
		con.setWebsiteLink(contactForm.getWebsiteLink());
		con.setLinkedInLink(contactForm.getLinkedInLink());

		// process image:

		if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
			logger.info("file is not empty");
			String fileName = UUID.randomUUID().toString();
			String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
			con.setCloudinaryImagePublicId(fileName);
			con.setPicturePath(imageUrl);
			contactForm.setPicture(imageUrl);

		} else {
			logger.info("file is empty");
		}

		var updateCon = contactService.update(con);
		logger.info("updated contact {}", updateCon);

		model.addAttribute("message", Message.builder().content("Contact Updated !!").type(MessageType.green).build());

		return "redirect:/user/contacts/view/" + contactId;
	}

}