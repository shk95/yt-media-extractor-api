package io.github.shk95.ytmediaextractorapi.service.image.uploader;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.ImageUploader;
import io.github.shk95.ytmediaextractorapi.service.RestClientTool;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageUploaderImpl implements ImageUploader {

	private final ApplicationProperties properties;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public ImageUploadResult upload(ImageExtracted imageExtracted, String title, String description) throws ImageUploadingException {
		ApplicationProperties.Image properties = this.properties.getImage();
		ImageUploader imageUploader = Providers.getInstance(new RestClientTool(restTemplate, objectMapper), properties);
		ImageUploadResult result;
		try {
			result = imageUploader.upload(imageExtracted, title, description);
		} catch (IllegalArgumentException e) {
			throw new ImageUploadingException(e.getMessage());
		} catch (ImageUploadingException e) {
			log.error("Image upload failed. [{}]", e.getMessage(), e);
			throw e;
		} finally {
			log.info("Delete image. [{}]", imageExtracted.delete());
		}
		log.info("Image upload completed. [{}]", result);
		return result;
	}

}
