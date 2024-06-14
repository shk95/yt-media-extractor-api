package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.imgur;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;
import lombok.Builder;

public class ImgurImageUploadResult extends ImageUploadResult {

	@Builder
	public ImgurImageUploadResult(
			String title,
			String description,
			String imgLink,
			String id,
			String deletehash,
			String clientId) {
		super(Providers.IMGUR, title, description, imgLink);
		super.getProviderData().put("provider", Providers.IMGUR.getProviderName());
		super.getProviderData().put("id", id);
		super.getProviderData().put("deletehash", deletehash);
		super.getProviderData().put("clientId", clientId);
	}

}
