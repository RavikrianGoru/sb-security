package com.rk.app.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Profile("security-jdbc")
@Configuration
@EnableWebSecurity
public class JdbcSecurityConfig {

	private final DataSource dataSource;

	public JdbcSecurityConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Value("${jdbc.user.details.query}")
	private String jdbcUserDetailsQuery;

	@Value("${jdbc.user.details.role.query}")
	private String jdbcUserDetailsRoleQuery;

	@Profile("security-jdbc")
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Profile("security-jdbc")
	@Bean
	public UserDetailsService userDetailsService() {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
		manager.setUsersByUsernameQuery(jdbcUserDetailsQuery);
		manager.setAuthoritiesByUsernameQuery(jdbcUserDetailsRoleQuery);
		return manager;
	}

	@Profile("security-jdbc")
	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder()); // ✅ Tells Spring how to validate password
		return authProvider;
	}

	@Profile("security-jdbc")
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager(); // ✅ Uses the configured provider with encoder
	}

	@Profile("security-jdbc")
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// declares which Page(URL) will have What access type
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/home").permitAll().requestMatchers("/h2-console/**")
				.permitAll().requestMatchers("/welcome").authenticated().requestMatchers("/admin").hasAuthority("ADMIN")
				.requestMatchers("/emp").hasAnyAuthority("EMPLOYEE").requestMatchers("/mgr").hasAnyAuthority("MANAGER")
				.requestMatchers("/common").hasAnyAuthority("EMPLOYEE", "MANAGER")
				// generally declared aunthenticated() in real time
				.anyRequest().authenticated())

				.formLogin(form -> form.defaultSuccessUrl("/welcome", true))

				// Logout Form Details
				.logout(logout -> logout.logoutUrl("/logout"))

				// Exception Details
				.exceptionHandling(ex -> ex.accessDeniedPage("/accessDenied"))

				// 🛠️ Allow H2 Console: disable CSRF & frame options for H2 only
				.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
				.headers(headers -> headers.frameOptions().sameOrigin());

		return http.build();
	}
}
