package io.github.shk95.ytmediaextractorapi.api;

import io.github.shk95.ytmediaextractorapi.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/link")
public class RestApiController {

	private final MainService mainService;

	@PostMapping("/image")
	// 유튜브 동영상 id, 시간 전달시 이미지 주소를 반환
	public ResponseEntity<?> extractImage(@RequestBody List<ItemRequest> itemRequests) {
		return ResponseEntity.ok(mainService.extractAndUpload(itemRequests));
	}

/*

	@DeleteMapping("/videos")
	// (작업)처리 완료후 필요없는 파일을 삭제
	public ResponseEntity<?> clenUp(@RequestParam String[] videoIds) {

		return null;
	}
*/

}
