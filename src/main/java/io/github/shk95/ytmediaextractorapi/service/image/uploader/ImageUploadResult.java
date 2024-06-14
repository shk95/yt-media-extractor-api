package io.github.shk95.ytmediaextractorapi.service.image.uploader;

import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public abstract class ImageUploadResult {

	private final String provider;
	private final String title;
	private final String description;
	private final String imgLink;
	private final Map<String, String> providerData = new HashMap<>();

	public ImageUploadResult(Providers provider, String title, String description, String imgLink) {
		this.provider = provider.getProviderName();
		this.title = title;
		this.description = description;
		this.imgLink = imgLink;
	}

	@Override
	public String toString() {
		return "ImageUploadResult{" +
				"provider='" + provider + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", imgLink='" + imgLink + '\'' +
				", providerData=" + providerData +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImageUploadResult that = (ImageUploadResult) o;
		return Objects.equals(provider, that.provider) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(imgLink, that.imgLink) && Objects.equals(providerData, that.providerData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(provider, title, description, imgLink, providerData);
	}

}
