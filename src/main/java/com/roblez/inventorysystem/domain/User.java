package com.roblez.inventorysystem.domain;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.roblez.inventorysystem.security.ValidPassword;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name="users")
public class User {

	@Id
	@Column(columnDefinition="uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable=false, unique=true, length=100)
	private String username;

	@Column(nullable=false, unique=true, length=100)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	private String name;	
	
	private String lastName;
	
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
        )
    @JsonManagedReference("role-users")
	private Set<Role> roles = new HashSet<>();
	
	private Instant joinedDate;
	
	private Instant lastConnection;
	
	private boolean active = false;

	// Constructores
	public User() {}
	
	public User(UUID id, String username, String email, String password, String lastName, String name,
			Set<Role> roles, Instant joinedDate, Instant lastConnection, boolean active) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.lastName = lastName;
		this.name = name;
		this.roles = roles;
		this.joinedDate = joinedDate;
		this.lastConnection = lastConnection;
		this.active = active;
	}
	
	//Getters y Setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(@ValidPassword String password) {
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Instant getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Instant joinedDate) {
		this.joinedDate = joinedDate;
	}

	public Instant getLastConnection() {
		return lastConnection;
	}

	public void setLastConnection(Instant lastConnection) {
		this.lastConnection = lastConnection;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	// Utils
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", lastName=" + lastName + ", name=" + name + ", roles=" + roles + ", joinedDate=" + joinedDate
				+ ", lastConnection=" + lastConnection + ", active=" + active + "]";
	}
}
