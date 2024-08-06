package info.ritesh.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import info.ritesh.scm.entity.User;
import info.ritesh.scm.exceptions.ResourceNotFoundException;
import info.ritesh.scm.helpers.AppConstants;
import info.ritesh.scm.repositories.UserRepository;
import info.ritesh.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	// add User repository dependency

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// add unimplemented methods

	@Override
	public User savUser(User user) {

		// generate unique id for user
		String userId = UUID.randomUUID().toString();
		user.setUserId(userId);

		// password encoding
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// set user-role
		user.setRoleList(List.of(AppConstants.ROLE_USER));

		user.setEnabled(true);

		// save user to database
		user = userRepository.save(user);
		return user;
	}

	@Override
	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Optional<User> updateUser(User user) {

		User tempUser = userRepository.findById(user.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found for Update"));
		tempUser.setName(user.getName());
		tempUser.setEmail(user.getEmail());
		tempUser.setPassword(user.getPassword());
		tempUser.setPhoneNumber(user.getPhoneNumber());
		tempUser.setAbout(user.getAbout());
		tempUser.setProfilePicPath(user.getProfilePicPath());
		tempUser.setEnabled(user.isEnabled());
		tempUser.setEmailVerified(user.isEmailVerified());
		tempUser.setPhoneVerified(user.isPhoneVerified());
		tempUser.setProvider(user.getProvider());
		tempUser.setProviderUserId(user.getProviderUserId());

		user = userRepository.save(tempUser);
		return Optional.of(user);
	}

	@Override
	public void deleteUser(String id) {
		userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for Deletion with id: " + id));
		userRepository.deleteById(id);
	}

	@Override
	public boolean isUserExist(String id) {
		User tempUser = userRepository.findById(id).orElse(null);
		return tempUser != null ? true : false;
	}

	@Override
	public boolean isUserExistByEmail(String email) {
		User tempUser = userRepository.findByEmail(email).orElse(null);
		return tempUser != null ? true : false;
	}

}
