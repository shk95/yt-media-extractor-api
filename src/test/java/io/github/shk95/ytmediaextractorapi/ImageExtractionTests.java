package io.github.shk95.ytmediaextractorapi;

import io.github.shk95.ytmediaextractorapi.service.ImageExtractor;
import io.github.shk95.ytmediaextractorapi.service.VideoDownloader;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoIntervalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@PropertySource("classpath:application-test.yml")
@SpringBootTest
@DisplayName("Image Extraction Tests")
public class ImageExtractionTests {


	@Autowired
	VideoDownloader videoDownloader;
	@Autowired
	ImageExtractor imageExtractor;

	@Test
	void test() {
		List<VideoIntervalTime> videoIntervalTimes = List.of(
				new VideoIntervalTime(LocalTime.parse("00:00:01")),
				new VideoIntervalTime(LocalTime.parse("00:00:05")),
				new VideoIntervalTime(LocalTime.parse("00:00:14"))
		);
		List<VideoInterval> videoIntervals = videoIntervalTimes.stream()
				.map(videoIntervalTime -> {
					try {
						return videoDownloader.download(UUID.randomUUID(), "jNQXAC9IVRw", videoIntervalTime);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				})
				.toList();
		videoIntervals.forEach(videoInterval -> {
			try {
				imageExtractor.extract(videoInterval);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

	}

}
