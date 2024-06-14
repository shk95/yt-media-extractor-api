package io.github.shk95.ytmediaextractorapi.config.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageUploaderValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageUploader {

	String message() default "Invalid uploader configuration.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
