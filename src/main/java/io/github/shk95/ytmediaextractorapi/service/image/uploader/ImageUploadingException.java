package io.github.shk95.ytmediaextractorapi.service.image.uploader;

public class ImageUploadingException extends Exception {

	private Boolean apiLimitExceeded = null;

	public ImageUploadingException(String message) {
		super(message);
	}

	public ImageUploadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public void setApiLimitExceededTrue() {
		this.apiLimitExceeded = true;
	}

	public boolean isApiLimitExceeded() {
		return (apiLimitExceeded != null) && apiLimitExceeded;
	}
}
