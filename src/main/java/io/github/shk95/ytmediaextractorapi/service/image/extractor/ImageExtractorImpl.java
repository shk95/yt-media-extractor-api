package io.github.shk95.ytmediaextractorapi.service.image.extractor;

import io.github.shk95.ytmediaextractorapi.service.ImageExtractor;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class ImageExtractorImpl implements ImageExtractor {

	@Override
	public ImageExtracted extract(VideoInterval videoInterval) throws IOException {
		File video = videoInterval.video(); // 이전 작업의 결과물.
		String videoFileAbsolutePath = video.getAbsolutePath();
		String videoId = videoInterval.videoId();

		DefaultExecutor executor = DefaultExecutor.builder().get();
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

		int exitValue = executor.execute(commandLine);

		log.info("Video file has deleted. [{}]", video.delete());
		if (exitValue != 0) {
			// 비디오 스트림이 없는등의 이유로 이미지를 추출할수없는경우.
			log.error("Image extractor exited with an error : [{}]\n[{}]", exitValue, outputStream);
			throw new IOException();
		}

		File image = new File(videoFileAbsolutePath + ".jpeg");
		ImageExtracted result = new ImageExtracted(videoId, image);
		log.info("Image extraction completed. path : [{}]", result);
		return result;
	}

}
