package com.roblez.inventorysystem.dto;

import java.util.Set;
import java.util.UUID;

import com.roblez.inventorysystem.domain.Role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
    @NotNull UUID userId,
    @NotEmpty Set<Role> roles
) {}
