package io.github.shk95.ytmediaextractorapi.service;

import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtractingException;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;

public interface ImageExtractor {

	ImageExtracted extract(VideoInterval videoInterval) throws ImageExtractingException;
}
