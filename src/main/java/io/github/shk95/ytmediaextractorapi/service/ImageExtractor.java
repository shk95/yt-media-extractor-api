package io.github.shk95.ytmediaextractorapi.service;

import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;

import java.io.IOException;

public interface ImageExtractor {

	ImageExtracted extract(VideoInterval videoInterval) throws IOException;
}
