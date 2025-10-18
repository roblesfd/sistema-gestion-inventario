package com.roblez.inventorysystem.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        // Ignorar endpoints de OpenAPI/Swagger para no interferir con su generación
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") || path.startsWith("/swagger-ui.html")) {
            throw ex; // re-lanzar para que springdoc/servlet maneje lo suyo
        }
		
		// Si el mensaje indica conflicto de SKU devuelve 409
		if(ex.getMessage() != null && ex.getMessage().contains("SKU")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}
		
		// Otros casos de IllegalArgumentException - 400 bad request
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}
