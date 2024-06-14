package io.github.shk95.ytmediaextractorapi.api;

import io.github.shk95.ytmediaextractorapi.service.MainService;
import io.github.shk95.ytmediaextractorapi.service.image.extractor.ImageExtracted;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploaderImpl;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.ImageUploadingException;
import io.github.shk95.ytmediaextractorapi.service.image.uploader.provider.imgur.ImgurImageUploadResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@PropertySource("classpath:application-test.yml")
@TestPropertySource(properties = {
		"app.image.uploader.use=imgur",
})
@DisplayName("Imgur Upload Test")
@AutoConfigureMockMvc
@SpringBootTest
class RestApiControllerTest {

	@Autowired
	MockMvc mockMvc;
	@MockBean
	RestTemplate restTemplate;
	@Autowired
	MainService mainService;
	String uuid = UUID.randomUUID().toString();
	String videoRequestsJson = """
			[
			    {
			        "videoId": "jNQXAC9IVRw",
			        "timestamp": "00:00:01",
			        "title": "testTitle",
			        "description": "testDescription"
			    }
			]""";
	String imageUploaderMockResponse = """
			{
			    "data": {
			        "id": "mockId",
			        "deletehash": "mockDeletehash",
			        "title": "mockTitle",
			        "description": "mockDescription",
			        "link": "mockImgLink"
			    }
			}""";
	@Captor
	private ArgumentCaptor<ImageExtracted> imageExtractedCaptor;
	@Captor
	private ArgumentCaptor<String> titleCaptor;
	@Captor
	private ArgumentCaptor<String> descriptionCaptor;

	@DisplayName("이미지 업로드 api 모킹")
	@Test
	void extractImageWillSuccess() throws Exception {
		given(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
				.willReturn(ResponseEntity.ok(imageUploaderMockResponse));
		mockMvc.perform(post("/api/v1/link/image")
						.contentType(MediaType.APPLICATION_JSON)
						.content(videoRequestsJson))
				.andExpect(status().isOk()).andDo(result -> {
					System.out.println(result.getResponse().getContentAsString());
				});
	}

	@Nested
	class ImageUploaderMocking {

		@MockBean
		ImageUploaderImpl imageUploader;

		@BeforeEach
		void setup() throws ImageUploadingException {
			given(imageUploader
					.upload(imageExtractedCaptor.capture(),
							titleCaptor.capture(),
							descriptionCaptor.capture()))
					.willAnswer(invocation -> {
								imageExtractedCaptor.getValue().delete();
								return ImgurImageUploadResult.builder()
										.id("imgur uploaded image id")
										.deletehash("imgur uploaded image deletehash")
										.imgLink("imgur uploaded image link")
										.clientId("imgur client id")
										.title(titleCaptor.getValue())
										.description(descriptionCaptor.getValue())
										.build();
							}
					);
		}

		@Test
		void extractImageWithMockedImageUploaderWillSuccess() throws Exception {
			System.out.println("uuid = " + uuid);
			mockMvc.perform(post("/api/v1/link/image")
							.contentType(MediaType.APPLICATION_JSON)
							.content(videoRequestsJson))
					.andExpect(status().isOk()).andDo(result -> {
						System.out.println("Response body : \n" + result.getResponse().getContentAsString());
					});
		}

	}

}
