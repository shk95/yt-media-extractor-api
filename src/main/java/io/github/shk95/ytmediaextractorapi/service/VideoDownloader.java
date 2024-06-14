package io.github.shk95.ytmediaextractorapi.service;

import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoDownloadingException;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoInterval;
import io.github.shk95.ytmediaextractorapi.service.video.downloader.VideoIntervalTime;

import java.util.UUID;

public interface VideoDownloader {

	VideoInterval download(final UUID fileNameUUID, final String videoId, VideoIntervalTime videoIntervalTime) throws VideoDownloadingException;

}
