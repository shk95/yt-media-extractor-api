package io.github.shk95.ytmediaextractorapi.service;

import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;

public interface ImageUploader {

	ImageUploadResult upload(ImageExtracted imageExtracted, String title, String description) throws ImageUploadingException;

}
