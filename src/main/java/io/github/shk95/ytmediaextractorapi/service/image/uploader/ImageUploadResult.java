package io.github.shk95.ytmediaextractorapi.service.image.uploader;

import java.util.Objects;

public record ImageUploadResult(
		String provider,
		String id,
		String deletehash,
		String title,
		String description,
		String link
) {

	@Override
	public String toString() {
		return "ImageUploadResult{" +
				"provider='" + provider + '\'' +
				", id='" + id + '\'' +
				", deletehash='" + deletehash + '\'' +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", link='" + link + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImageUploadResult that = (ImageUploadResult) o;
		return Objects.equals(id, that.id) && Objects.equals(link, that.link) && Objects.equals(title, that.title) && Objects.equals(provider, that.provider) && Objects.equals(deletehash, that.deletehash) && Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(provider, id, deletehash, title, description, link);
	}
}
