package com.roblez.inventorysystem.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roblez.inventorysystem.dto.RoleRequest;
import com.roblez.inventorysystem.dto.RoleResponse;
import com.roblez.inventorysystem.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController  {
	private final RoleService roleService;
	

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }
    
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable UUID id, @RequestBody RoleRequest request) {
    	return ResponseEntity.ok(roleService.updateRole(id, request));
    }
}
