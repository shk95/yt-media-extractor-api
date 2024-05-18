package io.github.shk95.ytmediaextractorapi.service;

import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;

public interface ImageUploader {

	ImageUploadResult upload(ImageExtracted imageExtracted) throws ImageUploadingException;

}
