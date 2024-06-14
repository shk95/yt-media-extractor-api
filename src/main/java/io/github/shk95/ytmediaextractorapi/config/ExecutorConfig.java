package io.github.shk95.ytmediaextractorapi.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@RequiredArgsConstructor
@Configuration
public class ExecutorConfig {

	private final ApplicationProperties properties;

	@Bean
	public ExecutorFactory executorFactory() {
		return new ExecutorFactory();
	}

	public class ExecutorFactory {

		public Executor createExecutor() {
			String workingDirectory = properties.getBasePath();
			return DefaultExecutor.builder().setWorkingDirectory(new File(workingDirectory)).get();
		}

	}

}
