package io.github.shk95.ytmediaextractorapi.service;

import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoIntervalTime;

import java.io.IOException;
import java.util.List;

public interface VideoDownloader {

	List<VideoInterval> download(final String workingDirectory, final String videoId,final List<VideoIntervalTime> videoIntervalTimes) throws IOException;

}
