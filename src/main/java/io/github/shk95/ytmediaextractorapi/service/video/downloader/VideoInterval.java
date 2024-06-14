package io.github.shk95.ytmediaextractorapi.service.video.downloader;

import java.io.File;

public record VideoInterval(
		File video
) {

	public boolean delete() {
		return this.video.delete();
	}

}
