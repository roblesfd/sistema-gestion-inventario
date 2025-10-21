package com.roblez.inventorysystem.dto;

import java.util.UUID;

import com.roblez.inventorysystem.security.ValidPassword;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
	    UUID id,
	    @NotBlank String username,
	    @NotBlank String email,
	    @NotBlank @ValidPassword String password
) {}