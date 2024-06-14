package io.github.shk95.ytmediaextractorapi.api;

import io.github.shk95.ytmediaextractorapi.service.MainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/link")
public class RestApiController {

	private final MainService mainService;

	// TODO: api id 등을 받아서 개인용 업로드가 가능하도록 추가

	@PostMapping("/image")
	// 유튜브 동영상 id, 시간 전달시 이미지 주소를 반환
	public ResponseEntity<List<VideoExtractionResult>> extractImage(@RequestBody @Valid List<@Valid VideoRequest> videoRequests) {
		return ResponseEntity.ok(mainService.extractAndUpload(videoRequests));
	}

}
