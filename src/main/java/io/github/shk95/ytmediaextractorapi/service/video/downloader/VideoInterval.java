package io.github.shk95.ytmediaextractorapi.service.video.downloader;

import java.io.File;

public record VideoInterval(
		String videoId,
		File video
) {

	boolean delete() {
		return this.video.delete();
	}

	@Override
	public String toString() {
		return "VideoInterval{" +
				"videoId='" + videoId + '\'' +
				", video=" + video.getAbsolutePath() +
				'}';
	}
}
