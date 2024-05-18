package io.github.shk95.ytmediaextractorapi.config.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageUploaderValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageUploaderValidation {

	String message() default "Invalid value";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
