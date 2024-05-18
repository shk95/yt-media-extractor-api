package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;

public class ImgurException extends ImageUploadingException {

	public ImgurException() {
	}

	public ImgurException(String message) {
		super(message);
	}

	public ImgurException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImgurException(Throwable cause) {
		super(cause);
	}

	public ImgurException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
