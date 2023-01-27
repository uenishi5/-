package com.example.youtubeDownload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResult {
	private String videoId;
	private String title;
	private String thumbnail;

	public SearchResult() {

	}
}
