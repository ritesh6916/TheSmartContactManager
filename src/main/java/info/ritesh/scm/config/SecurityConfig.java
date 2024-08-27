package info.ritesh.scm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import info.ritesh.scm.services.SecurityCustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

	@Autowired
	private SecurityCustomUserDetailsService userDetailService;

	@Autowired
	private OAuthAuthenicationSuccessHandler handler;

	@Autowired
	private AuthFailureHandler authFailureHandler;

	// configuraiton of authentication providerfor spring security
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		// user detail service ka object:
		daoAuthenticationProvider.setUserDetailsService(userDetailService);
		// password encoder ka object
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		// Exposing & restricting URLs based on needs
		httpSecurity.authorizeHttpRequests(authorize -> {
			authorize.requestMatchers("/user/**").authenticated();
			authorize.anyRequest().permitAll();
		});

		// form default login page
		httpSecurity.formLogin(formLogin -> {
			formLogin.loginPage("/login");
			formLogin.loginProcessingUrl("/authenticate");
			formLogin.defaultSuccessUrl("/user/profile");
			formLogin.failureUrl("/login?error=true");
			formLogin.usernameParameter("email");
			formLogin.passwordParameter("password");

			formLogin.failureHandler(authFailureHandler);

		});

		httpSecurity.csrf(AbstractHttpConfigurer::disable);

		httpSecurity.logout(logoutForm -> {
			logoutForm.logoutUrl("/logout");
			logoutForm.logoutSuccessUrl("/login?logout=true");
		});

		// Oauth2 configuration
		httpSecurity.oauth2Login(oauth -> {
			oauth.loginPage("/login");
			oauth.successHandler(handler);
			oauth.failureUrl("/login?error=true");
		});

		return httpSecurity.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
