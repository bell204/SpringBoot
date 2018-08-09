package org.zerock.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

// 확장자 가지고 이미지 타입인지 아닌지 판단해 주는 역할 하는 모듈
public class MediaUtils {
	private static Map<String, MediaType> mediaMap;

	static {

		mediaMap = new HashMap<String, MediaType>();
		mediaMap.put("JPG", MediaType.IMAGE_JPEG);
		mediaMap.put("GIF", MediaType.IMAGE_GIF);
		mediaMap.put("PNG", MediaType.IMAGE_PNG);
	}

	public static MediaType getMediaType(String type) {

		return mediaMap.get(type.toUpperCase());
	}
}