package info.ritesh.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

	public static String getEmailOfLoggedInUser(Authentication authentication) {

		if (authentication instanceof OAuth2AuthenticationToken) {

			var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
			var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

			var oauth2User = (OAuth2User) authentication.getPrincipal();
			String username = "";

			if (clientId.equalsIgnoreCase("google")) {

				// sign with google
				System.out.println("Helper: Getting email from google");
				username = oauth2User.getAttribute("email").toString();

			} else if (clientId.equalsIgnoreCase("github")) {

				// sign with github
				System.out.println("Helper: Getting email from github");
				username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
						: oauth2User.getAttribute("login").toString() + "@gmail.com";
			}

			return username;

		} else {
			System.out.println("Getting data from local database");
			return authentication.getName();
		}

	}
}
