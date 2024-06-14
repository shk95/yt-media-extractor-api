package io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.imgur;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import io.github.shk95.ytmediaextractorapi.service.RestClientTool;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadResult;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.BaseProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class Imgur extends BaseProvider {

	public Imgur(RestClientTool restClientTool, ApplicationProperties.Image properties) {
		super(restClientTool, properties);
	}

	@Override
	public ImageUploadResult upload(ImageExtracted imageExtracted, String title, String description) throws ImgurException, IllegalArgumentException {
		RestTemplate restTemplate = restClientTool.getClient();

		String url = "https://api.imgur.com/3/image";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		String clientId = super.properties.getUploader().getImgur().getClientId();
		Assert.notNull(clientId, "Imgur api client-id is null");
		headers.set("Authorization", "Client-ID " + clientId);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("image", new FileSystemResource(imageExtracted.image()));
		body.add("type", "file");
		body.add("title", title);
		body.add("description", description);

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
			if (statusCode.value() == 429) {
				// 요청을 일정 이상보내면 발생하는 api 허용량 오류. ( 429 Too Many Requests:"{" errors ":[{" id ":" */legacy - api - d99f64cb - 5 mk5x / I5fOvFdmK5 - 2427989 "," code ":" 429 "," status ":" Too Many Requests "," detail ":" Too Many Requests "}]}<EOL>")
				log.error("Api limit exceeded. [{}]", statusCode.value());
				ImgurException e = new ImgurException("Imgur upload failed. Api limit exceeded.");
				e.setApiLimitExceededTrue();
				throw e;
			}
			log.error("Api response status code is not OK. [{}]", statusCode.value());
			throw new ImgurException("Imgur upload failed.");
		}

		String responseBody = responseEntity.getBody();

		JsonNode rootNode;
		try {
			rootNode = super.restClientTool.getMapper().readTree(responseBody);
		} catch (JsonProcessingException e) {
			log.error("Json parsing failed. [{}]", e.getMessage(), e);
			throw new ImgurException("Imgur upload failed.");
		}
		log.debug("response body : {}", responseBody);
		String id = rootNode.path("data").path("id").asText();
		String deletehash = rootNode.path("data").path("deletehash").asText();
		String resultTitle = rootNode.path("data").path("title").asText();
		String resultDescription = rootNode.path("data").path("description").asText();
		String imgLink = rootNode.path("data").path("link").asText();
		return ImgurImageUploadResult.builder()
				.title(resultTitle)
				.description(resultDescription)
				.imgLink(imgLink)
				.id(id)
				.deletehash(deletehash)
				.clientId(clientId).build();
	}

}
