package io.mavg.challenge.controller.auth;

import io.mavg.challenge.config.security.JWTUtil;
import io.mavg.challenge.controller.auth.model.AuthRequest;
import io.mavg.challenge.controller.auth.model.AuthResponse;
import io.mavg.challenge.service.security.JwtUserDetailsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final AuthenticationManager authenticationManager;
	private final JwtUserDetailsService jwtUserDetailsService;
	private final JWTUtil jwtUtil;


	public AuthController(
			AuthenticationManager authenticationManager,
			JwtUserDetailsService jwtUserDetailsService,
			JWTUtil jwtUtil
	) {
		this.authenticationManager = authenticationManager;
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public AuthResponse login(
			@Valid @RequestBody AuthRequest request
	) {
		logger.info("Authenticating user: {}", request.getUsername());

		//TODO handle BadCredentialsException exception
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(),
						request.getPassword(),
						List.of()
				)
		);

		final UserDetails user = jwtUserDetailsService.loadUserByUsername(
				request.getUsername()
		);

		return new AuthResponse(
				jwtUtil.generateToken(user.getUsername())
		);
	}
}
