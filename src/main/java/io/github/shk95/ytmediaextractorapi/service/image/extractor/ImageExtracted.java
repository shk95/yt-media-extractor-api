package io.github.shk95.ytmediaextractorapi.service.image.extractor;

import java.io.File;

public record ImageExtracted(
		File image
) {

	public boolean delete() {
		return this.image.delete();
	}
}
