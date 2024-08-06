package info.ritesh.scm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import info.ritesh.scm.repositories.UserRepository;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// get user from database else throw Exception
		return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

	}

}
