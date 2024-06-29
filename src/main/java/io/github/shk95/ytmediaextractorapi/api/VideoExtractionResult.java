package io.github.shk95.ytmediaextractorapi.api;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import lombok.Getter;

public record VideoExtractionResult(
		String videoId,
		String timestamp,
		boolean success,
		ImageUploadResult result,
		ErrorResponse error
) {

	static final ErrorResponse NO_ERROR = new ErrorResponse(Error.NONE.code, Error.NONE.description);

	public static VideoExtractionResult success(String videoId, String timestamp, ImageUploadResult result) {
		return new VideoExtractionResult(videoId, timestamp, true, result, NO_ERROR);
	}

	public static VideoExtractionResult error(String videoId, String timestamp, Error error, String errorDescription) {
		ErrorResponse e = new ErrorResponse(error.code, error.description.concat(" ").concat(errorDescription));
		return new VideoExtractionResult(videoId, timestamp, false, null, e);
	}

	@Getter
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
		private final String description;

		Error(int code, String description) {
			this.code = code;
			this.description = description;
		}

	}

	private record ErrorResponse(
			int code,
			String description
	) {

	}

}
