package io.github.shk95.ytmediaextractorapi;

import io.github.shk95.ytmediaextractorapi.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootApplication
public class YtMediaExtractorApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(YtMediaExtractorApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
