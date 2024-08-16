package info.ritesh.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import info.ritesh.scm.entity.Providers;
import info.ritesh.scm.entity.User;
import info.ritesh.scm.helpers.AppConstants;
import info.ritesh.scm.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenicationSuccessHandler implements AuthenticationSuccessHandler {

	Logger logger = LoggerFactory.getLogger(OAuthAuthenicationSuccessHandler.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		logger.info("OAuth2 Authentication Success");

		DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
		logger.info(user.getName());

		user.getAttributes().forEach((key, value) -> {
			logger.info(key + " : " + value);
		});

		logger.info(user.getAttributes().toString());

		// Data saved to the database
		String email = user.getAttribute("email").toString();
		String name = user.getAttribute("name").toString();
		String picture = user.getAttribute("picture").toString();

		User newUser = new User();
		newUser.setEmail(email);
		newUser.setName(name);
		newUser.setProfilePicPath(picture);
		newUser.setPassword("Password");
		newUser.setUserId(UUID.randomUUID().toString());
		newUser.setProvider(Providers.GOOGLE);
		newUser.setEnabled(true);
		newUser.setEmailVerified(true);
		newUser.setProviderUserId(user.getName());
		newUser.setRoleList(List.of(AppConstants.ROLE_USER));
		newUser.setAbout("This account is created using Google signin feature.");

		User dbUser = userRepository.findByEmail(email).orElse(null);
		if (dbUser == null) {
			userRepository.save(newUser);
			logger.info("New User saved to the database: " + email);
		}

		// Redirecting to the profile page
		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

	}

}
