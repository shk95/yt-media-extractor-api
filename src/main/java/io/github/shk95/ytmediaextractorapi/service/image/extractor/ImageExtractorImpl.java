package io.github.shk95.ytmediaextractorapi.service.image.extractor;

import io.github.shk95.ytmediaextractorapi.config.ExecutorConfig;
import io.github.shk95.ytmediaextractorapi.service.ImageExtractor;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageExtractorImpl implements ImageExtractor {

	private final ExecutorConfig.ExecutorFactory executorFactory;

	@Override
	public ImageExtracted extract(VideoInterval videoInterval) throws ImageExtractingException {
		String videoFileAbsolutePath = videoInterval.video().getAbsolutePath();

		Executor executor = executorFactory.createExecutor();
		CommandLine commandLine = new CommandLine("ffmpeg");
		commandLine.addArgument("-i");
		commandLine.addArgument(videoFileAbsolutePath, true);
		commandLine.addArgument("-frames:v");
		commandLine.addArgument("1", true);
		commandLine.addArgument("-f");
		commandLine.addArgument("image2", true);
		commandLine.addArgument(videoFileAbsolutePath + ".jpeg", true);
		commandLine.addArgument("-y");
		log.debug("command : [{}] | arguments : [{}]", commandLine.getExecutable(), Arrays.toString(commandLine.getArguments()));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(pumpStreamHandler);

		int exitValue;
		try {
			exitValue = executor.execute(commandLine);
		} catch (IOException e) {
			log.error("Error occurred while executing image extraction command : [{}]", e.getMessage(), e);
			throw new ImageExtractingException("Error occurred while executing image extraction command.");
		}

		log.info("Video file has deleted. [{}]", videoInterval.delete());
		if (exitValue != 0) {
			// 비디오 스트림이 없는등의 이유로 이미지를 추출할수없는경우.
			log.error("Image extractor exited with an error : [{}]\n[{}]", exitValue, outputStream);
			throw new ImageExtractingException("Image extraction failed.");
		}

		File image = new File(videoFileAbsolutePath + ".jpeg");
		ImageExtracted result = new ImageExtracted(image);
		log.info("Image extraction completed. path : [{}]", result);
		return result;
	}

}
