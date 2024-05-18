package io.github.shk95.ytmediaextractorapi.api;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemResponse {

	private final String videoId;
	private final List<ImageUploadResult> data;

	public ItemResponse(String videoId, List<ImageUploadResult> data) {
		this.videoId = videoId;
		this.data = data;
	}


}
