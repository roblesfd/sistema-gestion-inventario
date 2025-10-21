package com.roblez.inventorysystem.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.roblez.inventorysystem.domain.User;

public interface UserRepository extends JpaRepository<User, UUID>{
	 User save(User user);
	 boolean existsByEmail(String email);
	 boolean existsByUsername(String email);
	 List<User> findAll();
	 Optional<User> findById(UUID id);
	 Optional<User> findByEmail(String email);
	 Optional<User> findByUsername(String username);
	 List<User> findByActiveTrue();
	 List<User> findByRoles_Name(String name);
	 void deleteById(UUID id);
	 @Modifying
	 @Query("UPDATE User u SET u.active = false WHERE u.id = :id")
	 void deactivateUser(@Param("id") UUID id);
}
