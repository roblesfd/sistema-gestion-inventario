package com.roblez.inventorysystem.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
	String message() default "La contraseña no cumple con los requisitos de seguridad (10 carácteres mínimo, al menos una minúscula, al menos una mayúscula, al menos un dígito, al menos un carácter especial)";
	Class<?>[] groups() default {};
	Class <? extends Payload>[] payload() default {};
}
