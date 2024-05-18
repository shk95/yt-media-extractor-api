package io.github.shk95.ytmediaextractorapi.service.video.downloader;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VideoIntervalTime {

	private final static DateTimeFormatter format = DateTimeFormatter.ofPattern("H:m:s");

	// H:m:s
	private final LocalTime timestamp;

	public VideoIntervalTime(String timestamp) {
		this.timestamp = LocalTime.parse(timestamp, format);
	}

	public String get() {
		return this.timestamp.format(format);
	}

	public String getMinimizedInterval() {
		return this.timestamp.plusSeconds(1).format(format);
	}

}
