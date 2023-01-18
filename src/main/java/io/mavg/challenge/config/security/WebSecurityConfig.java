package io.mavg.challenge.config.security;

import io.mavg.challenge.service.security.JwtUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebSecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	private final JwtUserDetailsService jwtUserDetailsService;

	public WebSecurityConfig(
			JwtAuthFilter jwtAuthFilter,
			JwtUserDetailsService jwtUserDetailsService
	) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.jwtUserDetailsService = jwtUserDetailsService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().disable();
		http.csrf().disable();
		http.formLogin().and().httpBasic().disable();

		http.securityMatcher("/api/**")
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.userDetailsService(jwtUserDetailsService)
				.exceptionHandling()
				.authenticationEntryPoint((request, response, ex) -> {
					System.out.println(ex);
					response.sendError(
							HttpServletResponse.SC_UNAUTHORIZED,
							ex.getMessage()
					);
				});

		http.authorizeHttpRequests(authorizeHttpRequests -> {
			authorizeHttpRequests
					.requestMatchers(
							"/v3/api-docs/**",
							"/swagger-ui/**",
							"/auth/**",
							"/actuator/**"
					)
					.permitAll()
					.requestMatchers("/api/**").authenticated()
					;
		});

		return http.build();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source =
				new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration
	) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
