package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.cloudinary;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;

public class CloudinaryException extends ImageUploadingException {

	public CloudinaryException(String message) {
		super(message);
	}

	public CloudinaryException(String message, Throwable cause) {
		super(message, cause);
	}

}
