package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.RestClientTool;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class Imgur extends BaseProvider {

	private final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

	public Imgur(RestClientTool restClientTool, ApplicationProperties.Image properties) {
		super(restClientTool, properties);
	}

	@Override
	public ImageUploadResult upload(ImageExtracted imageExtracted) throws ImgurException {
		RestTemplate restTemplate = restClientTool.getClient();

		String url = "https://api.imgur.com/3/image";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", "Client-ID " + super.properties.getUploader().getImgur().getClientId());

		body.add("image", new FileSystemResource(imageExtracted.image()));
		body.add("type", "file");

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<String> responseEntity = restTemplate.exchange(
				url,
				HttpMethod.POST,
				requestEntity,
				String.class
		);

		// 응답 확인
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode != HttpStatus.OK) {
			log.error("fail: {}, {}", statusCode, responseEntity.getBody());
			throw new ImgurException();
		}

		String responseBody = responseEntity.getBody();

		JsonNode rootNode;
		try {
			rootNode = super.restClientTool.getMapper().readTree(responseBody);
		} catch (JsonProcessingException e) {
			throw new ImgurException(e);
		}
		log.debug("response body : {}", responseBody);
		String id = rootNode.path("data").path("id").asText();
		String deletehash = rootNode.path("data").path("deletehash").asText();
		String title = rootNode.path("data").path("title").asText();
		String description = rootNode.path("data").path("description").asText();
		String link = rootNode.path("data").path("link").asText();
		return new ImageUploadResult("imgur",id, deletehash, title, description, link);
	}

	private void setTitle(String title) {
		this.body.add("title", title);
	}

	private void setDescription(String description) {
		this.body.add("description", description);
	}
}
