package com.example.youtubeDownload.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.youtubeDownload.entity.SearchResult;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Thumbnail;

import jp.ac.hcs.config.ApiKeyHolder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	// 取得する動画の数
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

	public static class YouTubeInstance {
		private static YouTube instance = null;

		public static YouTube singleton() {
			if (Objects.isNull(instance)) {
				instance = build();
			}

			return instance;
		}

		private static YouTube build() {
			return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
				@Override
				public void initialize(HttpRequest request) throws IOException {}
			}).setApplicationName("youtube-cmdline-search-sample").build();
		}
	}

	@Autowired
	private ApiKeyHolder holder;

	public List<Thumbnail> getList(String queryTerm) {
		try {
			return YouTubeInstance.singleton().search().list(Collections.singletonList("id,snippet")).setKey(this.holder.getYoutube()).setQ(queryTerm).setType(Collections.singletonList("video")).setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)").setMaxResults(NUMBER_OF_VIDEOS_RETURNED).execute().getItems().stream().map(searchResult -> {
				ResourceId rId = searchResult.getId();
				Thumbnail thumbnail = (Thumbnail) searchResult.getSnippet().getThumbnails().get("default");

				SearchResult result = new SearchResult();
				result.setVideoId(rId.getVideoId());
				result.setTitle(searchResult.getSnippet().getTitle());
				result.setThumbnail(thumbnail.getUrl());
				searchResult.getSnippet().getChannelTitle();

				return thumbnail;
			}).toList();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}
}
