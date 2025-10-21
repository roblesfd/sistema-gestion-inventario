package com.roblez.inventorysystem.repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roblez.inventorysystem.domain.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByName(String name);
    Optional<Role> findByName(String name);
	Set<Role> findByNameIn(Set<String> roleNames);
}