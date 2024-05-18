package io.github.shk95.ytmediaextractorapi.service.video.downloader;

import io.github.shk95.ytmediaextractorapi.service.VideoDownloader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class VideoDownloaderImpl implements VideoDownloader {

	@Override
	public List<VideoInterval> download(final String workingDirectory, final String videoId, final List<VideoIntervalTime> videoIntervalTimes) throws IOException {
		DefaultExecutor executor = DefaultExecutor.builder().setWorkingDirectory(new File(workingDirectory)).get();
		CommandLine commandLine = new CommandLine("yt-dlp");
		commandLine.addArgument("--ies");
		commandLine.addArgument("youtube*", true);
		commandLine.addArgument(videoId);
		commandLine.addArgument("-o");
		commandLine.addArgument("%(id)s-%(autonumber)s.%(ext)s", true);
		commandLine.addArgument("-f");
		commandLine.addArgument("bv*[protocol*=https]", true);
		commandLine.addArgument("--print");
		commandLine.addArgument("after_move:filepath");
		commandLine.addArgument("--no-warning");
		commandLine.addArgument("-q");
		commandLine.addArgument("--force-overwrites");
		videoIntervalTimes
				.forEach(intervalTime -> {
							commandLine.addArgument("--download-sections");
							commandLine.addArgument("*" + intervalTime.get() + "-" + intervalTime.getMinimizedInterval(), true);
						}
				);
		log.debug("command : [{}] | arguments : [{}]", commandLine.getExecutable(), Arrays.toString(commandLine.getArguments()));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
		executor.setStreamHandler(pumpStreamHandler);

		int exitValue = executor.execute(commandLine);
		if (exitValue != 0) {
			/* TODO:
			    설정된 추출시간들에 따른 여러 동영상을 다운로드 받던 도중 알수없는 이유로 종료되어도 다운로드 과정 전체가 실패하지 않음.
			    이때 남는 파일을 삭제해야 하는 문제. (거의 일어날일이 없다.)
			 */
			log.error("An error occurred while downloading the video. Exit with : [{}]\nError output : [{}]",
					exitValue, outputStream);
			throw new IOException();
		}

		String output = outputStream.toString();
		List<String> paths = Arrays.stream(output.split("\n"))
				.filter(p -> p.startsWith("/"))
				.toList();
		log.debug(output);

		List<VideoInterval> videoIntervals = new ArrayList<>();
		paths.forEach(p -> videoIntervals.add(new VideoInterval(videoId, new File(p))));

		log.info("Video download completed. [{}]", videoIntervals);
		return videoIntervals;
	}

}
