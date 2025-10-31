package com.roblez.inventorysystem.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.config.UserMapper;
import com.roblez.inventorysystem.domain.Role;
import com.roblez.inventorysystem.domain.User;
import com.roblez.inventorysystem.dto.ChangePasswordRequest;
import com.roblez.inventorysystem.dto.UpdateUserRequest;
import com.roblez.inventorysystem.dto.UserRequest;
import com.roblez.inventorysystem.dto.UserResponse;
import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.repository.RoleRepository;
import com.roblez.inventorysystem.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;

@Service
@Transactional
public class UserService {
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final UserMapper mapper;
	private final PasswordEncoder passwordEncoder;
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	
	public UserService(UserRepository userRepo, UserMapper mapper, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.mapper = mapper;
		this.passwordEncoder = passwordEncoder;
	}
	
	public UserResponse createUser(UserRequest request) {
		if(userRepo.existsByEmail(request.email())) {
			throw new IllegalArgumentException("El correo ya esta registrado");
		}
		
		if(userRepo.existsByUsername(request.username())) {
			throw new IllegalArgumentException("El nombre de usuario ya esta existe");
		}
		
		User user = mapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(request.password()));
		userRepo.save(user);
		log.info("Usuario creado -> ID: {}, Nombre de usuario: {}, Email:{}",user.getId(), user.getUsername(), user.getEmail());
		
		return mapper.toDto(user);
	}
	
	public UserResponse getUserById(UUID id) {
		User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con ese id"));
	
		return mapper.toDto(user);
	}

	public List<UserResponse> getAllUsers() {
		return userRepo.findAll().stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}
	
	public UserResponse updateUser(UpdateUserRequest request) {
			User user = userRepo.findById(request.id()).orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con ese id"));
			
		    if (!user.getUsername().equals(request.username()) && userRepo.existsByUsername(request.username())) {
		        throw new IllegalArgumentException("Nombre de usuario ya en uso");
		    }
		    if (!user.getEmail().equals(request.email()) && userRepo.existsByEmail(request.email())) {
		        throw new IllegalArgumentException("Email ya en uso");
		    }
		    
		    Set<Role> foundRoles = roleRepo.findByNameIn(request.roles());
		    
		    if (foundRoles.size() != request.roles().size()) {
		        throw new IllegalArgumentException("Algunos roles no existen en la base de datos");
		    }
			
			user.setUsername(request.username());
			user.setEmail(request.email());
			user.setRoles(foundRoles);
			user.setName(request.name());
			user.setLastName(request.lastname());
			user.setActive(request.active());
			
		    if (request.password() != null && !request.password().isBlank()) {
		        user.setPassword(passwordEncoder.encode(request.password()));
		    }
		    
		    User updated = userRepo.save(user);
			log.info("Usuario actualizado -> ID: {}, Nombre de usuario: {}, Email:{}",user.getId(), user.getUsername(), user.getEmail());

		    return mapper.toDto(updated);
	}

	public void deleteUser(UUID id) {
		User user = userRepo.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Usuario no encontrado con id: " + id));
		
		userRepo.deleteById(user.getId());
		
		log.info("Usuario eliminado -> ID: {}, Nombre de usuario: {}, Email:{}",user.getId(), user.getUsername(), user.getEmail());
	}

	public UserResponse findByUsernameOrEmail(String query) {
		User user = userRepo.findByUsername(query)
					 .or(() -> userRepo.findByEmail(query))
                     .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
		return mapper.toDto(user);
	}

	public void changePassword(UUID id, ChangePasswordRequest request) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
	   user.setPassword(passwordEncoder.encode(request.newPassword()));
	   userRepo.save(user);		
	}

	public UserResponse toggleUserStatus(UUID id, boolean active) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
	   user.setActive(active);
	   return mapper.toDto(userRepo.save(user));
	}

	public UserResponse updateUserRoles(UUID id, @NotEmpty Set<Role> roles) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
	   user.setRoles(roles);
	   return mapper.toDto(userRepo.save(user));
	}
}
