package com.roblez.inventorysystem.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.roblez.inventorysystem.domain.Role;
import com.roblez.inventorysystem.domain.User;
import com.roblez.inventorysystem.exception.ResourceNotFoundException;
import com.roblez.inventorysystem.report.ColumnDescriptor;
import com.roblez.inventorysystem.report.GenericReportRow;
import com.roblez.inventorysystem.report.ReportSchema;
import com.roblez.inventorysystem.repository.UserRepository;

@Service
public class UserReportProvider {
	private final UserRepository repo;
	
	public UserReportProvider(UserRepository repo) {
		this.repo = repo;
	}
	
	// Schema púplico para que los renderers lo usen
	public ReportSchema schema() {
		return new ReportSchema(List.of(
				new ColumnDescriptor("username", "Nombre de usuario", Objects::toString),
				new ColumnDescriptor("name", "Nombre", Objects::toString),
				new ColumnDescriptor("lastName", "Apellido", Objects::toString),
				new ColumnDescriptor("email", "Correo electrónico", Objects::toString),
				new ColumnDescriptor("roles", "Roles", Objects::toString),
				new ColumnDescriptor("active", "activo", Objects::toString)
				
		));
	}
	

	public List<GenericReportRow> fetch() {
		List<User> users = repo.findAll();
		
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron usuarios");
        }
        
        List<GenericReportRow> rows = users.stream().map(u -> {
            GenericReportRow r = new GenericReportRow();
            Set<Role> roles = u.getRoles(); // si es LAZY, considera fetch/join en repo
            r.put("username", u.getUsername());
            r.put("name", u.getName());
            r.put("lastName", u.getLastName());
            r.put("email", u.getEmail());
            r.put("roles", roles.stream().map(Role::getName).collect(Collectors.joining	(", ")));
            r.put("active", u.isActive());
 
            return r;
        }).collect(Collectors.toList());
        
        return rows;
	}	
}
