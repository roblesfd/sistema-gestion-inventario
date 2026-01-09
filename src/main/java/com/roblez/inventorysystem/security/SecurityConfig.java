package com.roblez.inventorysystem.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@EnableWebSecurity
@Configuration
public class SecurityConfig {	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Value("inventory_api.url")
    private String inventoryApiUrl;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
        	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(inventoryApiUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
