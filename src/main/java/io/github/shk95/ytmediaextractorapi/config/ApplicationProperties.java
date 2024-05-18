package io.github.shk95.ytmediaextractorapi.config;

import io.github.shk95.ytmediaextractorapi.config.validation.ImageUploaderValidation;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "app")
public final class ApplicationProperties {

	@NotEmpty
	private String youtubeUrlPrefix;
	@NotEmpty
	private String basePath;
	private Image image;

	@Setter
	@Getter
	public static class Image {

		private Uploader uploader;

		@Setter
		@Getter
		public static class Uploader {

			@ImageUploaderValidation
			private String use;
			private Imgur imgur;

			@Setter
			@Getter
			public static class Imgur {

				private String clientId;

			}

		}
	}
}
