package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.cloudinary;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;
import lombok.Builder;

public class CloudinaryImageUploadResult extends ImageUploadResult {

	@Builder
	public CloudinaryImageUploadResult(
			String title,
			String description,
			String imgLink) {
		super(Providers.CLOUDINARY, title, description, imgLink);
		super.getProviderData().put("provider", Providers.CLOUDINARY.getProviderName());
	}

}
