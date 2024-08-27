package info.ritesh.scm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import info.ritesh.scm.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	/*
	 * We can perform all the CRUD operations using the methods provided by
	 * JpaRepository
	 */

	// extra methods for custom queries

	// custom finder methods
	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndPassword(String email, String password);
	
	Optional<User> findByEmailToken(String emailToken);

}
