package com.roblez.inventorysystem.security;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String>{
	@Override
	public void initialize(ValidPassword constraintAnnotation) {}
	
	@Override 
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if(password == null) return false;
		
		boolean lengthValid = password.length() >= 10 && password.length() <= 64;
		boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
		boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
		boolean hasDigit = password.chars().anyMatch(Character::isDigit);
		boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+[]{}|;:,.<>?".indexOf(ch) >= 0);
 	
        return lengthValid && hasUpper && hasLower && hasDigit && hasSpecial;
	}
}
