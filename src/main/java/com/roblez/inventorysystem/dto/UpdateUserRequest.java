package com.roblez.inventorysystem.dto;

import java.util.Set;
import java.util.UUID;

import com.roblez.inventorysystem.domain.Role;

public record UpdateUserRequest (
	    UUID id,
	    String username,
	    String email,
	    String password,
	    Set<Role> roles,
	    String name,
	    String lastname,
	    boolean active
) {}