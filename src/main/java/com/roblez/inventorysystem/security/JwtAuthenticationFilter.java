package com.roblez.inventorysystem.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.service.CustomUserDetailsService;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailService;
    
	public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailService) {
		this.jwtUtil = jwtUtil;
		this.userDetailService = userDetailService;
	}

    @Override 
    protected void doFilterInternal(HttpServletRequest request,
    							    HttpServletResponse response,
    							    FilterChain filterChain) throws ServletException, IOException {
    	String authHeader = request.getHeader("Authorization");
    	String token = null;
    	
    	if(authHeader != null && authHeader.startsWith("Bearer ")) {
    		token = authHeader.substring(7);
    		Optional<String>  optUsername = jwtUtil.extractUsername(token);
    		
    		if(optUsername.isPresent()) {
                String username = optUsername.get();
    			
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails;
                    try {
                        userDetails = userDetailService.loadUserByUsername(username);
                    } catch (ResourceNotFoundException e) {
                        filterChain.doFilter(request, response);
                        return;
                    }

                    if (jwtUtil.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } 
                }
			}
    		
    		
    	}
    	
    	filterChain.doFilter(request, response);
    }

}
