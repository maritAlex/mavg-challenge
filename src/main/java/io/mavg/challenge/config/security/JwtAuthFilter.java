package io.mavg.challenge.config.security;

import io.mavg.challenge.service.security.JwtUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUserDetailsService userDetailsService;

	private final JWTUtil jwtUtil;

	public JwtAuthFilter(JwtUserDetailsService userDetailsService, JWTUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
	) throws ServletException, IOException {
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if(header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String token = header.split(" ")[1].trim();

		if(jwtUtil.isTokenExpired(token) ) {
			filterChain.doFilter(request, response);
			return;
		}

		final UserDetails user = userDetailsService
				.loadUserByUsername(
						jwtUtil.getUsernameFromToken(token)
				);

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(
						user,
						user.getPassword(),
						List.of()
				);

		authenticationToken.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
		);

		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		filterChain.doFilter(request, response);
	}
}
