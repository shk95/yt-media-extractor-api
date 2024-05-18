package io.github.shk95.ytmediaextractorapi.service.image.extractor;

import java.io.File;

public record ImageExtracted(
		String videoId,
		File image) {

	boolean delete() {
		return this.image.delete();
	}

	@Override
	public String toString() {
		return "ImageExtracted{" +
				"videoId='" + videoId + '\'' +
				", image=" + image.getAbsolutePath() +
				'}';
	}
}
