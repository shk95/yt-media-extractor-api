package io.github.shk95.ytmediaextractorapi.service;

import io.github.shk95.ytmediaextractorapi.api.VideoExtractionResult;
import io.github.shk95.ytmediaextractorapi.api.VideoRequest;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtractingException;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoDownloadingException;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoIntervalTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MainService {

	private final VideoDownloader videoDownloader;
	private final ImageExtractor imageExtractor;
	private final ImageUploader imageUploader;

	public MainService(VideoDownloader videoDownloader, ImageExtractor imageExtractor, ImageUploader imageUploader) {
		this.videoDownloader = videoDownloader;
		this.imageExtractor = imageExtractor;
		this.imageUploader = imageUploader;
	}

	/*
	 * TODO: Executor 에서 발생하는 오류로 인해 처리되지 않고 남는 파일을 삭제해야 하는 문제. (거의 일어날일이 없다.)
	 *   -> 오래된 파일들을 정기적으로 삭제하도록 하기.
	 * */
	public List<VideoExtractionResult> extractAndUpload(List<VideoRequest> videoRequests) {
		return videoRequests.stream()
				.map(videoRequest -> {
					final String videoId = videoRequest.videoId();
					final String timestamp = videoRequest.timestamp();
					final UUID uuid = UUID.randomUUID();
					final String title = videoRequest.title();
					final String description = videoRequest.description();
					log.info("Extracting videoId: {}, timestamp: {}, uuid: {}", videoId, timestamp, uuid);

					VideoExtractionResult videoExtractionResult;
					try {
						VideoIntervalTime videoIntervalTime = new VideoIntervalTime(LocalTime.parse(videoRequest.timestamp()));
						VideoInterval videoInterval = videoDownloader.download(uuid, videoId, videoIntervalTime);
						ImageExtracted imageExtracted = imageExtractor.extract(videoInterval);
						ImageUploadResult imageUploadResult = imageUploader.upload(imageExtracted, title, description);
						videoExtractionResult = VideoExtractionResult
								.success(videoId, timestamp, imageUploadResult);
					} catch (DateTimeParseException e) {
						log.error("Timestamp parsing failed for videoId: {}, timestamp: {}, uuid: {}", videoId, timestamp, uuid, e);
						return VideoExtractionResult.error(videoId, timestamp,
								VideoExtractionResult.Error.TIMESTAMP_PARSING_FAILED, e.getMessage());
					} catch (VideoDownloadingException e) {
						log.error("Video Downloading failed for videoId: {}, timestamp: {}, uuid: {}", videoId, timestamp, uuid, e);
						return VideoExtractionResult.error(videoId, timestamp,
								VideoExtractionResult.Error.VIDEO_DOWNLOAD_FAILED, e.getMessage());
					} catch (ImageExtractingException e) {
						log.error("Image Extracting failed for videoId: {}, timestamp: {}, uuid: {}", videoId, timestamp, uuid, e);
						return VideoExtractionResult.error(videoId, timestamp,
								VideoExtractionResult.Error.IMAGE_EXTRACT_FAILED, e.getMessage());
					} catch (ImageUploadingException e) {
						log.error("Image Uploading failed for videoId: {}, timestamp: {}, uuid: {}", videoId, timestamp, uuid, e);
						if (e.isApiLimitExceeded()) {
							return VideoExtractionResult.error(videoId, timestamp,
									VideoExtractionResult.Error.IMAGE_UPLOAD_FAILED_BY_API_LIMIT, e.getMessage());
						} else {
							return VideoExtractionResult.error(videoId, timestamp,
									VideoExtractionResult.Error.IMAGE_UPLOAD_FAILED, e.getMessage());
						}
					} catch (Exception e) {
						log.error("Unknown error occurred for videoId: {}, timestamp: {}, uuid: {}", videoId, timestamp, uuid, e);
						return VideoExtractionResult.error(videoId, timestamp,
								VideoExtractionResult.Error.UNKNOWN, e.getMessage());
					}
					return videoExtractionResult;
				})
				.toList();
	}

}
