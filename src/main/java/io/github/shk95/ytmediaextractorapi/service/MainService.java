package io.github.shk95.ytmediaextractorapi.service;

import io.github.shk95.ytmediaextractorapi.api.ItemRequest;
import io.github.shk95.ytmediaextractorapi.api.ItemResponse;
import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoIntervalTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MainService {

	private final VideoDownloader videoDownloader;
	private final ImageExtractor imageExtractor;
	private final ImageUploader imageUploader;
	private final ApplicationProperties properties;
	private final String BASE_PATH;

	public MainService(VideoDownloader videoDownloader, ImageExtractor imageExtractor, ImageUploader imageUploader, ApplicationProperties properties) {
		this.videoDownloader = videoDownloader;
		this.imageExtractor = imageExtractor;
		this.imageUploader = imageUploader;
		this.properties = properties;
		this.BASE_PATH = properties.getBasePath();
	}

	public List<ItemResponse> extractAndUpload(List<ItemRequest> itemRequests) {
		return itemRequests.stream()
				.map(itemRequest -> {
					List<VideoInterval> videoIntervals;
					try {
						videoIntervals = videoDownloader.download(
								BASE_PATH,
								itemRequest.getVideoId(),
								itemRequest.getTimestamp().stream()
										.map(VideoIntervalTime::new)
										.toList());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}

					List<ImageExtracted> imageExtractedList = new ArrayList<>();
					videoIntervals
							.forEach(videoInterval -> {
								try {
									ImageExtracted imageExtracted = imageExtractor.extract(videoInterval);
									imageExtractedList.add(imageExtracted);
								} catch (IOException e) {
									throw new RuntimeException(e);
								}
							});

					List<ImageUploadResult> imageUploadResults = new ArrayList<>();
					imageExtractedList
							.forEach(imageExtracted -> {
								try {
									imageUploadResults.add(imageUploader.upload(imageExtracted));
								} catch (ImageUploadingException e) {
									throw new RuntimeException(e);
								}
							});
					return new ItemResponse(itemRequest.getVideoId(), imageUploadResults);
				})
				.toList();
	}
}
