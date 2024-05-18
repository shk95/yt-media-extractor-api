package io.github.shk95.ytmediaextractorapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.client.RestTemplate;

@Getter
public class RestClientTool {

	private final RestTemplate client;
	private final ObjectMapper mapper;

	public RestClientTool(RestTemplate restTemplate, ObjectMapper objectMapper) {
		this.client = restTemplate;
		this.mapper = objectMapper;
	}

}
