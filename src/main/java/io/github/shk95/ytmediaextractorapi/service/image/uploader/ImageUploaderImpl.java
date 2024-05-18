package io.github.shk95.ytmediaextractorapi.service.image.uploader;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.RestClientTool;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.ImageUploader;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.ImgurException;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.Providers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageUploaderImpl implements ImageUploader {

	private final ApplicationProperties properties;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public ImageUploadResult upload(ImageExtracted imageExtracted) throws ImageUploadingException {
		File image = imageExtracted.image(); // 이전 작업의 결과물.
		ApplicationProperties.Image properties = this.properties.getImage();
		ImageUploader imageUploader =
				Providers.getInstance(new RestClientTool(restTemplate, objectMapper), properties);
		ImageUploadResult result;
		try {
			result = imageUploader.upload(imageExtracted);
		} catch (ImgurException e) {
			log.error("");
			throw new ImageUploadingException();
		} finally {
			log.info("Delete image. [{}]", image.delete());
		}
		log.info("Image upload completed. [{}]", result);
		return result;
	}

}
