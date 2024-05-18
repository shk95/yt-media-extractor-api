package io.github.shk95.ytmediaextractorapi.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ItemRequest {

	public String videoId;
	public List<String> timestamp;

}
