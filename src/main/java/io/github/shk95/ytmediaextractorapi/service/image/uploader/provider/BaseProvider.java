package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider;

import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.ImageUploader;
import io.github.shk95.ytmediaextractorapi.service.RestClientTool;

public abstract class BaseProvider implements ImageUploader {

	protected final RestClientTool restClientTool;
	protected final ApplicationProperties.Image properties;

	public BaseProvider(RestClientTool restClientTool, ApplicationProperties.Image properties) {
		this.restClientTool = restClientTool;
		this.properties = properties;
	}

}
