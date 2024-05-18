package io.github.shk95.ytmediaextractorapi.config.validation;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageUploaderValidator implements ConstraintValidator<ImageUploaderValidation, String> {

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return Providers.containsProviderName(s);
	}
}
