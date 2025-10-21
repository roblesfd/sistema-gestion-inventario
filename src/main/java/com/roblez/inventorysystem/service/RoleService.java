package com.roblez.inventorysystem.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.config.RoleMapper;
import com.roblez.inventorysystem.domain.Role;
import com.roblez.inventorysystem.dto.RoleRequest;
import com.roblez.inventorysystem.dto.RoleResponse;
import com.roblez.inventorysystem.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {
	private final RoleRepository roleRepo;
	private final RoleMapper mapper;
	
    public RoleService(RoleRepository roleRepo, RoleMapper mapper) {
        this.roleRepo = roleRepo;
        this.mapper = mapper;
    }
    
    public RoleResponse createRole(RoleRequest request) {
    	if (roleRepo.existsByName(request.name())) {
    		throw new IllegalArgumentException("Rol ya existe");
    	}
    	
    	Role role = mapper.toEntity(request);
    	return mapper.toDto(roleRepo.save(role));
    }
    
    public List<RoleResponse> getAllRoles() {
    	return roleRepo.findAll().stream()
    			.map(mapper::toDto)
    			.collect(Collectors.toList());
    }
    
    public RoleResponse getRoleById(UUID id) {
    	Role role = roleRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ese id"));
    	
    	return mapper.toDto(role);
    }
    
    public RoleResponse updateRole(UUID id, RoleRequest request) {
    	Role role = roleRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ese id"));
    	role.setName(request.name());
    	role.setDescription(request.description());
    	
    	return mapper.toDto(roleRepo.save(role));
    }
    
    public void deleteRole(UUID id) {
    	Role role = roleRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("No existe un usuario con ese id"));

       roleRepo.deleteById(role.getId());
    }
    
}
