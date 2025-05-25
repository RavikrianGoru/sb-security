package com.rk.app.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Profile("security-inmem")
@Configuration
@EnableWebSecurity
public class InMemSecurityConfig {

	@Profile("security-inmem")
	@Bean
	protected InMemoryUserDetailsManager configAuthentication() {
		// Define all users
		List<UserDetails> users = new ArrayList<>();

		// Define authorities and add to user
		// 1.
		List<GrantedAuthority> adminAuthority = new ArrayList<>();
		adminAuthority.add(new SimpleGrantedAuthority("ADMIN"));
		UserDetails admin = new User("devs", "{noop}devs", adminAuthority);
		users.add(admin);

		// 2.
		List<GrantedAuthority> empAuthority = new ArrayList<>();
		empAuthority.add(new SimpleGrantedAuthority("EMPLOYEE"));
		UserDetails emp = new User("ns", "{noop}ns", empAuthority);
		users.add(emp);

		// 3.
		List<GrantedAuthority> managerAuthority = new ArrayList<>();
		managerAuthority.add(new SimpleGrantedAuthority("MANAGER"));
		UserDetails manager = new User("vs", "{noop}vs", managerAuthority);
		users.add(manager);

		return new InMemoryUserDetailsManager(users);
	}

	@Profile("security-inmem")
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// declares which Page(URL) will have What access type
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/home").permitAll().requestMatchers("/welcome")
				.authenticated().requestMatchers("/admin").hasAuthority("ADMIN").requestMatchers("/emp")
				.hasAnyAuthority("EMPLOYEE").requestMatchers("/mgr").hasAnyAuthority("MANAGER")
				.requestMatchers("/common").hasAnyAuthority("EMPLOYEE", "MANAGER")
				// generally declared aunthenticated() in real time
				.anyRequest().authenticated())

				.formLogin(form -> form.defaultSuccessUrl("/welcome", true))

				// Logout Form Details
				.logout(logout -> logout.logoutUrl("/logout"))

				// Exception Details
				.exceptionHandling(ex -> ex.accessDeniedPage("/accessDenied"));

		return http.build();
	}
}
