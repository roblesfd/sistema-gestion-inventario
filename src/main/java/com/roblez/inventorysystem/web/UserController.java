package com.roblez.inventorysystem.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roblez.inventorysystem.dto.ChangePasswordRequest;
import com.roblez.inventorysystem.dto.UpdateUserRequest;
import com.roblez.inventorysystem.dto.UpdateUserRoleRequest;
import com.roblez.inventorysystem.dto.UserRequest;
import com.roblez.inventorysystem.dto.UserResponse;
import com.roblez.inventorysystem.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request ) {
		return ResponseEntity.ok(userService.createUser(request));
	}
	
	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}
	
	@GetMapping("/search")
    @Operation(summary = "Buscar usuario", description = "Buscar un usuario por nombre de usuario o email")
	public ResponseEntity<UserResponse> getUserByUsernameOrEmail(@RequestParam String query) {
		return ResponseEntity.ok(userService.findByUsernameOrEmail(query));
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
		return ResponseEntity.ok(userService.updateUser(request));
	}
	
	@PatchMapping("/{id}/password")
    @Operation(summary = "Cambiar contraseña", description = "Cambia el campo password del usuario")
	public ResponseEntity<Void> changePassword(@PathVariable UUID id, @RequestBody ChangePasswordRequest request) {
		userService.changePassword(id, request);
		
		return ResponseEntity.noContent().build();
	}
	
	//Desactivar cuenta con active=false
	@PatchMapping("/{id}/status")
    @Operation(summary = "Desactivar una cuenta", description = "Desactiva una cuenta de usuario con active=false")
	public ResponseEntity<UserResponse> toggleUserStatus(@PathVariable UUID id, @RequestParam boolean active) {
	    return ResponseEntity.ok(userService.toggleUserStatus(id, active));
	}
	
	@PatchMapping(path = "/{id}/roles")
    @Operation(summary = "Actualizar roles", description = "Actualiza el campo role del usuario")
	public ResponseEntity<UserResponse> updateRoles(@PathVariable UUID id, @RequestBody UpdateUserRoleRequest request) {
	    return ResponseEntity.ok(userService.updateUserRoles(id, request.roles()));
	}
	
	@DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina la cuenta de usuario")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
