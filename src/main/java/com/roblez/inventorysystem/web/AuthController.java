package com.roblez.inventorysystem.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roblez.inventorysystem.dto.LoginRequest;
import com.roblez.inventorysystem.dto.UserRequest;
import com.roblez.inventorysystem.dto.UserResponse;
import com.roblez.inventorysystem.security.JwtUtil;
import com.roblez.inventorysystem.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    
	public AuthController(AuthenticationManager authManager, UserService userService, JwtUtil jwtUtil) {
		this.authManager = authManager;
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.username(), request.password())
				);
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String token = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(Map.of("token", token));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(@RequestBody @Valid UserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}
}
