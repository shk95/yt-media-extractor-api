package io.github.shk95.ytmediaextractorapi.api;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

public record VideoRequest(
		@NotEmpty
		String videoId,
		@NotEmpty
		String timestamp, // ISO 8601 HH:mm:ss
		@Nullable
		String title,
		@Nullable
		String description
) {

}
