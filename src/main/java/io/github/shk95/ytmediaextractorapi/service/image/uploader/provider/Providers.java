package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider;

import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.ImageUploader;
import io.github.shk95.ytmediaextractorapi.service.RestClientTool;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.cloudinary.Cloudinary;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.imgur.Imgur;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.BiFunction;

@Getter
public enum Providers {
	IMGUR("imgur", Imgur::new),
	CLOUDINARY("cloudinary", Cloudinary::new);

	Providers(String providerName,
	          BiFunction<RestClientTool, ApplicationProperties.Image, ? extends BaseProvider> providerInstance) {
		this.providerName = providerName;
		this.providerInstance = providerInstance;
	}

	public static ImageUploader getInstance(RestClientTool restClientTool, ApplicationProperties.Image properties) {
		return Arrays.stream(Providers.values())
				.filter(provider -> provider == properties.getUploader().getUse())
				.findFirst()
				.map(provider -> provider.providerInstance.apply(restClientTool, properties))
				.orElseThrow(); // 프로퍼티 검증단계에서 확인됨.
	}

	private final String providerName;
	private final BiFunction<RestClientTool, ApplicationProperties.Image, ? extends BaseProvider> providerInstance;

	@Override
	public String toString() {
		return "Providers{" +
				"providerName='" + providerName +
				'}';
	}

}
