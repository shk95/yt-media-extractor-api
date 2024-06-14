package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.cloudinary;

import com.cloudinary.utils.ObjectUtils;
import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.RestClientTool;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.BaseProvider;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;

import java.util.Map;

public class Cloudinary extends BaseProvider {

	public Cloudinary(RestClientTool restClientTool, ApplicationProperties.Image properties) {
		super(restClientTool, properties);
	}

	private com.cloudinary.Cloudinary setup() throws CloudinaryException {
		if (super.properties.getUploader().getUse() != Providers.CLOUDINARY) {
			throw new CloudinaryException("Cloudinary properties not found");
		}
		String environmentVariable = super.properties.getUploader().getCloudinary().getEnvironmentVariable();
		String cloudName = super.properties.getUploader().getCloudinary().getCloudName();
		String apiKey = super.properties.getUploader().getCloudinary().getApiKey();
		String apiSecret = super.properties.getUploader().getCloudinary().getApiSecret();
		if (environmentVariable != null) {
			return new com.cloudinary.Cloudinary(environmentVariable);
		} else if (cloudName != null && apiKey != null && apiSecret != null) {
			return new com.cloudinary.Cloudinary(com.cloudinary.utils.ObjectUtils.asMap(
					"cloud_name", cloudName,
					"api_key", apiKey,
					"api_secret", apiSecret
			));
		} else {
			throw new CloudinaryException("Cloudinary properties not found");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ImageUploadResult upload(ImageExtracted imageExtracted, String title, String description) throws ImageUploadingException {
		com.cloudinary.Cloudinary cloudinary = this.setup();
		String imgUrl;
		try {
			Map result = cloudinary.uploader().upload(imageExtracted.image(), ObjectUtils.emptyMap());
			imgUrl = (String) result.get("secure_url");
		} catch (Exception e) {
			throw new CloudinaryException(e.getMessage(), e);
		}
		return CloudinaryImageUploadResult.builder()
				.title(null)
				.description(null)
				.imgLink(imgUrl)
				.build();
	}

}
