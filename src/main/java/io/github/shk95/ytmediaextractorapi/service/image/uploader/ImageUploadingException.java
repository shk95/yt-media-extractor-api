package io.github.shk95.ytmediaextractorapi.service.image.uploader;

public class ImageUploadingException extends Exception {

	public ImageUploadingException() {
	}

	public ImageUploadingException(Throwable cause) {
		super(cause);
	}

	public ImageUploadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ImageUploadingException(String message) {
		super(message);
	}

	public ImageUploadingException(String message, Throwable cause) {
		super(message, cause);
	}
}
