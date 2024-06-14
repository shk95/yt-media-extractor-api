package io.github.shk95.ytmediaextractorapi.config;

import io.github.shk95.ytmediaextractorapi.config.validation.ValidImageUploader;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;
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
		@ValidImageUploader
		public static class Uploader {

			private Providers use;
			private Imgur imgur;
			private Cloudinary cloudinary;

			@Setter
			@Getter
			public static class Imgur {

				private String clientId;

			}

			@Setter
			@Getter
			public static class Cloudinary {

				private String cloudName;
				private String apiKey;
				private String apiSecret;
				private String environmentVariable;

			}
		}
	}
}
