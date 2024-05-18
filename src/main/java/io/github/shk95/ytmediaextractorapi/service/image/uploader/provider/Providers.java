package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider;

import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.ImageUploader;
import io.github.shk95.ytmediaextractorapi.service.RestClientTool;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum Providers {
	IMGUR("imgur", Imgur::new);

	Providers(String name,
	          BiFunction<RestClientTool, ApplicationProperties.Image, ? extends BaseProvider> providerInstance) {
		this.providerName = name;
		this.providerInstance = providerInstance;
	}

	private final String providerName;
	private final BiFunction<RestClientTool, ApplicationProperties.Image, ? extends BaseProvider> providerInstance;

	public static boolean containsProviderName(String name) {
		return Arrays.stream(Providers.values())
				.anyMatch(provider -> provider.providerName.equals(name));
	}

	public static ImageUploader getInstance(RestClientTool restClientTool, ApplicationProperties.Image properties) {
		return Arrays.stream(Providers.values())
				.filter(provider -> provider.providerName.equals(properties.getUploader().getUse()))
				.findFirst()
				.map(provider -> provider.providerInstance.apply(restClientTool, properties))
				.orElseThrow(); // 프로퍼티 검증단계에서 확인됨.
	}

}
