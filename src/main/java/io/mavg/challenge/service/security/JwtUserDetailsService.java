package io.mavg.challenge.service.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JwtUserDetailsService implements UserDetailsService {

	private static final String OPERATOR_USERNAME = "operator";
	private final String passwordEncrypted;

	public JwtUserDetailsService(PasswordEncoder passwordEncoder) {
		this.passwordEncrypted = passwordEncoder.encode("secret");
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(OPERATOR_USERNAME.equals(username)) {
			return new User(
					OPERATOR_USERNAME,
					passwordEncrypted,
					new ArrayList<>()
			);
		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}
}
