package info.ritesh.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

		// identify the provider

		var oauth2AuthenicationToken = (OAuth2AuthenticationToken) authentication;

		String authorizedClientRegistrationId = oauth2AuthenicationToken.getAuthorizedClientRegistrationId();

		logger.info(authorizedClientRegistrationId);

		var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

		oauthUser.getAttributes().forEach((key, value) -> {
			logger.info(key + " : " + value);
		});

		User user = new User();
		user.setUserId(UUID.randomUUID().toString());
		user.setRoleList(List.of(AppConstants.ROLE_USER));
		user.setEmailVerified(true);
		user.setEnabled(true);
		user.setPassword("Dummy");

		if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

			// google
			// google attributes

			user.setEmail(oauthUser.getAttribute("email").toString());
			user.setProfilePicPath(oauthUser.getAttribute("picture").toString());
			// user.setProfilePicPath("https://cdn.dribbble.com/users/5534/screenshots/14230133/profile_4x.jpg");
			user.setName(oauthUser.getAttribute("name").toString());
			user.setProviderUserId(oauthUser.getName());
			user.setProvider(Providers.GOOGLE);
			user.setAbout("This account is created using google.");

		} else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

			// github
			// github attributes
			String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
					: oauthUser.getAttribute("login").toString() + "@gmail.com";
			String picture = oauthUser.getAttribute("avatar_url").toString();
			String name = oauthUser.getAttribute("login").toString();
			String providerUserId = oauthUser.getName();

			user.setEmail(email);
			user.setProfilePicPath(authorizedClientRegistrationId);
			user.setName(name);
			user.setProviderUserId(providerUserId);
			user.setProvider(Providers.GITHUB);

			user.setAbout("This account is created using github");
		}

		else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {

		}

		else {
			logger.info("OAuthAuthenicationSuccessHandler: Unknown provider");
		}

		/*
		 * DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
		 * logger.info(user.getName());
		 * 
		 * user.getAttributes().forEach((key, value) -> { logger.info(key + " : " +
		 * value); });
		 * 
		 * logger.info(user.getAttributes().toString());
		 * 
		 * // Data saved to the database String email =
		 * user.getAttribute("email").toString(); String name =
		 * user.getAttribute("name").toString(); String picture =
		 * user.getAttribute("picture").toString();
		 * 
		 * User newUser = new User(); newUser.setEmail(email); newUser.setName(name);
		 * newUser.setProfilePicPath(picture); newUser.setPassword("Password");
		 * newUser.setUserId(UUID.randomUUID().toString());
		 * newUser.setProvider(Providers.GOOGLE); newUser.setEnabled(true);
		 * newUser.setEmailVerified(true); newUser.setProviderUserId(user.getName());
		 * newUser.setRoleList(List.of(AppConstants.ROLE_USER));
		 * newUser.setAbout("This account is created using Google signin feature.");
		 * 
		 * User dbUser = userRepository.findByEmail(email).orElse(null); if (dbUser ==
		 * null) { userRepository.save(newUser);
		 * logger.info("New User saved to the database: " + email); }
		 */

		User dbUser = userRepository.findByEmail(user.getEmail()).orElse(null);
		if (dbUser == null) {
			userRepository.save(user);
			logger.info("New User saved to the database: " + user.getEmail());
		}

		// Redirecting to the profile page
		new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

	}

}
