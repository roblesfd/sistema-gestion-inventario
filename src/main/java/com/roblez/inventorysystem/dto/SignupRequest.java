package com.roblez.inventorysystem.dto;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
	@NotBlank String username,
	@NotBlank String email, 
	@NotBlank String password
) {}