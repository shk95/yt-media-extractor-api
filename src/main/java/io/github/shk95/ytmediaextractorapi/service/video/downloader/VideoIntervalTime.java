package io.github.shk95.ytmediaextractorapi.service.video.downloader;

import java.time.LocalTime;

public record VideoIntervalTime(
		LocalTime timestamp
) {

	public long get() {
		return this.timestamp.toSecondOfDay();
	}

	public long getMinimizedInterval() {
		return this.timestamp.plusSeconds(1).toSecondOfDay();
	}

}
