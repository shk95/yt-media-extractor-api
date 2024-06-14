package io.github.shk95.ytmediaextractorapi.config.validation;

import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageUploaderValidator implements ConstraintValidator<ValidImageUploader, ApplicationProperties.Image.Uploader> {

	@Override
	public boolean isValid(ApplicationProperties.Image.Uploader uploader, ConstraintValidatorContext context) {
		if (uploader == null) return false;
		Providers provider = uploader.getUse();
		if (provider == null) return false;
		switch (provider) {
			case IMGUR -> {
				return validateImgur(uploader.getImgur(), context);
			}
			case CLOUDINARY -> {
				return validateCloudinary(uploader.getCloudinary(), context);
			}
			default -> {
				return false;
			}
		}
	}

	private boolean validateImgur(ApplicationProperties.Image.Uploader.Imgur imgur, ConstraintValidatorContext context) {
		if (imgur == null || imgur.getClientId() == null || imgur.getClientId().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Imgur clientId must be provided. ").addConstraintViolation();
			return false;
		}
		return true;
	}

	private boolean validateCloudinary(ApplicationProperties.Image.Uploader.Cloudinary cloudinary, ConstraintValidatorContext context) {
		if (cloudinary.getEnvironmentVariable() != null && !cloudinary.getEnvironmentVariable().isEmpty()) {
			return true;
		}
		if ((cloudinary.getApiKey() != null && !cloudinary.getApiKey().isEmpty()) &&
				(cloudinary.getApiSecret() != null && !cloudinary.getApiSecret().isEmpty()) &&
				(cloudinary.getCloudName() != null && !cloudinary.getCloudName().isEmpty())) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("Cloudinary properties must be provided. ").addConstraintViolation();
		return false;
	}

}
