package com.roblez.inventorysystem.config;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.roblez.inventorysystem.domain.User;
import com.roblez.inventorysystem.dto.UpdateUserRequest;
import com.roblez.inventorysystem.dto.UserRequest;
import com.roblez.inventorysystem.dto.UserResponse;

@Mapper(componentModel="spring")
public interface UserMapper {
	
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	User toEntity(UserRequest request);
	User toUpdateEntity(UpdateUserRequest request, @MappingTarget User entity);
	UserResponse toDto(User user);
}
