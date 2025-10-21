package com.roblez.inventorysystem.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    	String username = null;
    	
    	if(authHeader != null && authHeader.startsWith("Bearer ")) {
    		token = authHeader.substring(7);
    		username = jwtUtil.extractUsername(token).get();
    	}
    	
    	if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    		UserDetails userDetails = userDetailService.loadUserByUsername(username); 
    		
    		if(jwtUtil.isTokenValid(token, userDetails)) {
    			UsernamePasswordAuthenticationToken authToken = 
    					new UsernamePasswordAuthenticationToken(
    							userDetails, null, userDetails.getAuthorities()
    					);
                SecurityContextHolder.getContext().setAuthentication(authToken);
    		}
    	}
    	
    	filterChain.doFilter(request, response);
    }

}
