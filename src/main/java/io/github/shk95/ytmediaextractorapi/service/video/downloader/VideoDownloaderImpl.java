package io.github.shk95.ytmediaextractorapi.service.video.downloader;

import io.github.shk95.ytmediaextractorapi.config.ExecutorConfig;
import io.github.shk95.ytmediaextractorapi.service.VideoDownloader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class VideoDownloaderImpl implements VideoDownloader {

	private final ExecutorConfig.ExecutorFactory executorFactory;

	private static CommandLine getCommandLine(UUID fileNameUUID, String videoId, VideoIntervalTime videoIntervalTime) {
		CommandLine commandLine = new CommandLine("yt-dlp");
		commandLine.addArgument("--ies");
		commandLine.addArgument("youtube*", true);
		commandLine.addArgument(videoId);
		commandLine.addArgument("-o");
		commandLine.addArgument(videoId + "_" + fileNameUUID.toString() + ".%(ext)s", true);
		commandLine.addArgument("-f");
		commandLine.addArgument("bv*[protocol*=https]", true);
		commandLine.addArgument("--print");
		commandLine.addArgument("after_move:filepath");
		commandLine.addArgument("--no-warning");
		commandLine.addArgument("-q");
		commandLine.addArgument("--force-overwrites");
		commandLine.addArgument("--download-sections");
		commandLine.addArgument("*" + videoIntervalTime.get() + "-" + videoIntervalTime.getMinimizedInterval(), true);
		return commandLine;
	}

	private static boolean checkFile(UUID fileNameUUID, String videoId, String resultFileName) {
		if (resultFileName == null) return false;
		if (resultFileName.isEmpty()) return false;
		if (resultFileName.indexOf("/") != 0) return false;
		return resultFileName.contains(videoId + "_" + fileNameUUID.toString() + ".");
	}

	@Override
	public VideoInterval download(final UUID fileNameUUID, final String videoId, VideoIntervalTime videoIntervalTime) throws VideoDownloadingException {
		Executor executor = executorFactory.createExecutor();
		CommandLine commandLine = getCommandLine(fileNameUUID, videoId, videoIntervalTime);
		log.debug("command : [{}] | arguments : [{}]", commandLine.getExecutable(), Arrays.toString(commandLine.getArguments()));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(pumpStreamHandler);
		executor.setWatchdog(ExecuteWatchdog.builder().setTimeout(Duration.ofSeconds(60)).get());

		final int exitValue;
		try {
			exitValue = executor.execute(commandLine);
		} catch (IOException e) {
			log.error("An error occurred while downloading the video.", e);
			throw new VideoDownloadingException("An error occurred while downloading the video.");
		}

		if (exitValue != 0) {
			log.error("An error occurred while downloading the video. Exit with : [{}]\nError output : [{}]",
					exitValue, outputStream);
			throw new VideoDownloadingException("Video download failed. Maybe the video does not exist.");
		}

		String resultFileAbsPath = outputStream.toString().strip();
		if (!checkFile(fileNameUUID, videoId, resultFileAbsPath)) {
			log.error("The downloaded file does not match the requested file. [output : {}]", resultFileAbsPath);
			throw new VideoDownloadingException("Video download failed.");
		}

		VideoInterval videoInterval = new VideoInterval(new File(resultFileAbsPath));
		log.info("Video download completed. [{}]", videoInterval.video().getAbsolutePath());
		return videoInterval;
	}

}
