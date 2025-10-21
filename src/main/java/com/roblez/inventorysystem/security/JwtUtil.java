package com.roblez.inventorysystem.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String SECRET_KEY;
	
	public String generateToken(UserDetails userDetails) {

		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.claim("roles", userDetails.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority).toList())
				.setIssuedAt(new Date())
				.setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.compact();				
	}
	
	public Optional<String> extractUsername(String token) {
		try {
			return Optional.of(
					Jwts.parserBuilder()
						.setSigningKey(SECRET_KEY.getBytes())
						.build()
						.parseClaimsJws(token)
						.getBody()
						.getSubject()
					);
		}catch(Exception e) {
			return Optional.empty();
		}
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		return extractUsername(token)
				.map(username -> username.equals(userDetails.getUsername()) && !isTokenExpired(token))
				.orElse(false);

	}	
	
	private boolean isTokenExpired(String token) {
		try {
			Date expiration = Jwts.parserBuilder()
					.setSigningKey(SECRET_KEY.getBytes())
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getExpiration();
			return expiration.before(new Date());
		}catch(Exception e) {
			return true; // token invalido
		}
	}
}
