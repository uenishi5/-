package jp.ac.hcs.mbraw;

import java.util.Objects;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;

public class YouTubeInstance {
	private static YouTube instance = null;

	public static YouTube singleton() {
		if (Objects.isNull(instance)) {
			instance = build();
		}

		return instance;
	}

	private static YouTube build() {
		return new YouTube.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(),
				request -> {}).setApplicationName("youtube-cmdline-search-sample").build();
	}
}
