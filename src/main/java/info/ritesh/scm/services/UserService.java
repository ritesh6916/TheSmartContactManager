package info.ritesh.scm.services;

import java.util.List;
import java.util.Optional;

import info.ritesh.scm.entity.User;

public interface UserService {

	// We will treat email as userName

	// CRUD operations
	User savUser(User user);

	Optional<User> getUserById(String id);

	List<User> getAllUsers();

	Optional<User> updateUser(User user);

	void deleteUser(String id);

	// Other operations
	boolean isUserExist(String id);

	boolean isUserExistByEmail(String email);

	User getUserByEmail(String email);

}
