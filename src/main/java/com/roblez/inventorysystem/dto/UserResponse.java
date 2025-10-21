package com.roblez.inventorysystem.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.roblez.inventorysystem.domain.Role;

import jakarta.validation.constraints.NotBlank;

public record UserResponse(
	UUID id,
	@NotBlank String username,
	@NotBlank String email,
	@NotBlank String password,
	String name,
	String lastName,
	Set<Role> roles,
	Instant joinedDate,
	Instant lastConnection
) {}