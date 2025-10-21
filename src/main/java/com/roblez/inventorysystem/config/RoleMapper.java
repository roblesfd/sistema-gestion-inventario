package com.roblez.inventorysystem.config;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.roblez.inventorysystem.domain.Role;
import com.roblez.inventorysystem.dto.RoleRequest;
import com.roblez.inventorysystem.dto.RoleResponse;

@Mapper(componentModel="spring")
public interface RoleMapper {
	RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
	
	Role toEntity(RoleRequest dto);
	RoleResponse toDto(Role entity);
}
