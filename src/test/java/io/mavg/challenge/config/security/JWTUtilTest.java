package io.mavg.challenge.config.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {

	private final JWTUtil jwtUtil = new JWTUtil("DKsPf9tCuP6FSDTvvosuQ47oqBdMqyWS");

	@Test
	public void shouldCreateAJWTTokenGivenTheUsername() {
		final String token = jwtUtil.generateToken("mavg");

		assertEquals("mavg", jwtUtil.getUsernameFromToken(token));
		assertFalse(jwtUtil.isTokenExpired(token));
	}

}