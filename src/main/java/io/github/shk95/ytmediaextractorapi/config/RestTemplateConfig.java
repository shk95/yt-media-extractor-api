package io.github.shk95.ytmediaextractorapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setReadTimeout(Duration.ofSeconds(20))
				.setConnectTimeout(Duration.ofSeconds(20))
				.build();
	}

	@Primary
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
