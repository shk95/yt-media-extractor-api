package io.github.shk95.ytmediaextractorapi.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import lombok.Getter;

public record VideoExtractionResult(
		String videoId,
		String timestamp,
		boolean success,
		ImageUploadResult result,
		Error error
) {

	public static VideoExtractionResult success(String videoId, String timestamp, ImageUploadResult result) {
		return new VideoExtractionResult(videoId, timestamp, true, result, Error.NONE);
	}

	public static VideoExtractionResult error(String videoId, String timestamp, Error error) {
		return new VideoExtractionResult(videoId, timestamp, false, null, error);
	}

	@Getter
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum Error {
		UNKNOWN(-1, "Unknown error."),
		NONE(0, "No error."),
		VIDEO_DOWNLOAD_FAILED(1, "Video download failed."),
		TIMESTAMP_PARSING_FAILED(11, "Timestamp parsing failed."),
		IMAGE_EXTRACT_FAILED(2, "Image extraction failed."),
		IMAGE_UPLOAD_FAILED(3, "Image upload failed."),
		IMAGE_UPLOAD_FAILED_BY_API_LIMIT(31, "Uploader api limit exceeded. Try to upload later."),
		;

		private final int code;
		private String description;

		Error(int code, String description) {
			this.code = code;
			this.description = description;
		}

		public Error addDescription(String description) {
			this.description = this.description.concat(" " + description);
			return this;
		}
	}

}
