package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.imgur;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;

public class ImgurException extends ImageUploadingException {

	public ImgurException(String message) {
		super(message);
	}

	@Override
	public void setApiLimitExceededTrue() {
		super.setApiLimitExceededTrue();
	}

}
