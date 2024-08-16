package info.ritesh.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import info.ritesh.scm.services.SecurityCustomUserDetailsService;

@Configuration
public class SecurityConfig {

	@Autowired
	private SecurityCustomUserDetailsService userDetailService;

	@Autowired
	private OAuthAuthenicationSuccessHandler handler;

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
			formLogin.defaultSuccessUrl("/user/dashboard");
			formLogin.failureUrl("/login?error=true");
			formLogin.usernameParameter("email");
			formLogin.passwordParameter("password");

			/*
			 * formLogin.failureHandler(new AuthenticationFailureHandler() {
			 * 
			 * @Override public void onAuthenticationFailure(HttpServletRequest request,
			 * HttpServletResponse response, AuthenticationException exception) throws
			 * IOException, ServletException { // TODO Auto-generated method stub } });
			 * 
			 * formLogin.successHandler(new AuthenticationSuccessHandler() {
			 * 
			 * @Override public void onAuthenticationSuccess(HttpServletRequest request,
			 * HttpServletResponse response, Authentication authentication) throws
			 * IOException, ServletException { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * });
			 */
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
