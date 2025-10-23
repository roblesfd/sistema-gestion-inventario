package com.roblez.inventorysystem.security;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
public class SecurityConfig {	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/api/auth/**").permitAll()
					
					.requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("ADMIN", "MANAGER")
					.requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole("ADMIN", "MANAGER")
					.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyRole("ADMIN", "MANAGER")
					.requestMatchers(HttpMethod.PATCH, "/api/products/{id}/stock").hasAnyRole("ADMIN", "MANAGER")

					.requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("ADMIN")
					
					.requestMatchers(HttpMethod.POST, "/api/roles/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PATCH, "/api/roles/**").hasRole("ADMIN")
					
					.requestMatchers(HttpMethod.POST, "/api/suppliers/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PUT, "/api/suppliers/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.DELETE, "/api/suppliers/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.PATCH, "/api/suppliers/**").hasRole("ADMIN")
					
					.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs", "/v3/api-docs/**").permitAll()
					.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
