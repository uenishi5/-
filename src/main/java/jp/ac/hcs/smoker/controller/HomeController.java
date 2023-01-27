package jp.ac.hcs.smoker.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.ren130302.utils.DateUtils;
import com.ren130302.utils.DateUtils.Unit;
import com.ren130302.webapi.newsapi.NewsApiClient;
import com.ren130302.webapi.newsapi.response.Article;
import com.ren130302.webapi.pornhubapi.PornhubApiClient;
import com.ren130302.webapi.pornhubapi.response.SearchResponse;
import com.ren130302.webapi.pornhubapi.response.VideoResponse;

import jp.ac.hcs.config.ApiKeyHolder;
import jp.ac.hcs.config.Mapping;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

@Slf4j
@Controller
public class HomeController {

	@Autowired
	private ApiKeyHolder holder;

	@GetMapping(Mapping.MAPPING_ROOT)
	public String getHome(Principal principal, Model model, Pageable pageable) {
		log.debug("GET {}", Mapping.MAPPING_ROOT);
		return Mapping.RESOURCE_ROOT;
	}

	@PostMapping(Mapping.MAPPING_NEWSAPI)
	@ResponseBody
	public String postNewsApi(Pageable pageable, @Validated NewsForm form, BindingResult bindingResult) {
		log.debug("POST {}", Mapping.MAPPING_NEWSAPI);
		log.debug("Request params:{}", form);
		log.debug("page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

		final ResponseBodyContents responseBodyContents = new ResponseBodyContents();

		if (bindingResult.hasErrors()) {
			responseBodyContents.setAllErrors(bindingResult.getAllErrors());
			return responseBodyContents.json();
		}

		// NewsApi問い合わせ
		try {
			Response<Article.Response> response = NewsApiClient.EVERYTHING.ready(this.holder.getNews(), b -> form.query(b).page(pageable.getPageNumber()).pageSize(pageable.getPageSize())).execute();
			log.debug("{}", response.raw().request().url().toString());

			if (response.isSuccessful()) {
				final Article.Response articleResponse = Objects.nonNull(response.body()) ? response.body() : new Article.Response();
				responseBodyContents.map(articleResponse);
				return responseBodyContents.json();
			}
			else {
				log.debug("{}", response.errorBody().string());
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return responseBodyContents.json();
	}

	@PostMapping(Mapping.MAPPING_PORNHUBAPI)
	@ResponseBody
	public String postPornHub(Pageable pageable, @Validated PornhubForm form, BindingResult bindingResult) {
		log.debug("POST {}", Mapping.MAPPING_PORNHUBAPI);
		log.debug("Request params:{}", form);
		log.debug("page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

		final ResponseBodyContents responseBodyContents = new ResponseBodyContents();

		if (bindingResult.hasErrors()) {
			responseBodyContents.setAllErrors(bindingResult.getAllErrors());
			return responseBodyContents.json();
		}

		// PornhubApi問い合わせ
		try {
			Response<SearchResponse> response = PornhubApiClient.SEARCH.ready(b -> form.query(b).page(pageable.getPageNumber())).execute();
			log.debug("{}", response.raw().request().url().toString());

			if (response.isSuccessful()) {
				final SearchResponse searchResponse = Objects.nonNull(response.body()) ? response.body() : new SearchResponse();
				responseBodyContents.map(searchResponse);
				return responseBodyContents.json();
			}
			else {
				log.debug("{}", response.errorBody().string());
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return responseBodyContents.json();
	}

	@PostMapping(Mapping.MAPPING_YOUTUBEAPI)
	@ResponseBody
	public String postYoutube(Pageable pageable, @Validated YoutubeForm form, BindingResult bindingResult) {
		log.debug("POST {}", Mapping.MAPPING_YOUTUBEAPI);
		log.debug("Request params:{}", form);
		log.debug("page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

		final ResponseBodyContents responseBodyContents = new ResponseBodyContents();

		if (bindingResult.hasErrors()) {
			responseBodyContents.setAllErrors(bindingResult.getAllErrors());
			return responseBodyContents.json();
		}

		// PornhubApi問い合わせ
		try {
			Response<SearchResponse> response = PornhubApiClient.SEARCH.ready(b -> form.query(b).page(pageable.getPageNumber())).execute();
			log.debug("{}", response.raw().request().url().toString());

			if (response.isSuccessful()) {
				final SearchResponse searchResponse = Objects.nonNull(response.body()) ? response.body() : new SearchResponse();
				responseBodyContents.map(searchResponse);
				return responseBodyContents.json();
			}
			else {
				log.debug("{}", response.errorBody().string());
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return responseBodyContents.json();
	}

	@Data
	public static class ResponseBodyContents {

		private List<ObjectError> allErrors;
		private List<Content> contents;

		@Data
		public static class Content {

			private String originalUrl;
			private String iconUrl;
			private String title;
			private String source;
			private String publishBy;
			private String publishAt;

			public static Content map(SearchResult searchResult) {
				final Content content = new Content();

				final ResourceId resourceId = searchResult.getId();
				final SearchResultSnippet searchResultSnippet = searchResult.getSnippet();
				final Thumbnail thumbnailDefault = searchResultSnippet.getThumbnails().getDefault();

				final SearchResult result = new SearchResult();
				searchResult.getSnippet().getChannelTitle();

				content.setOriginalUrl(String.format("https://www.youtube.com/watch?v=%S", resourceId));
				content.setIconUrl(thumbnailDefault.getUrl());
				content.setTitle(searchResultSnippet.getTitle());
				content.setSource(String.format("https://www.youtube.com/watch?v=%S", resourceId));
				content.setPublishBy(searchResult.getSnippet().getChannelTitle());
				content.setPublishAt(searchResult.getSnippet().getPublishedAt().toString());
				return content;
			}

			public static Content map(Article article) {
				Content content = new Content();
				content.setOriginalUrl(article.getUrl());
				content.setIconUrl(article.getUrlToImage());
				content.setTitle(article.getTitle());
				content.setSource(article.getUrl());
				content.setPublishBy(article.getAuthor());
				content.setPublishAt(article.getPublishedAt());
				return content;
			}

			public static Content map(VideoResponse videoResponse) {
				Content content = new Content();

				String publishBy = "";

				try {
					publishBy = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss").parse(videoResponse.getPublishDate()).toInstant().toString();
				}
				catch (ParseException e) {
					e.printStackTrace();
				}

				String publishAt = videoResponse.getPornstars().isEmpty() ? "" : videoResponse.getPornstars().get(0).getPornstarName();

				content.setOriginalUrl(videoResponse.getUrl());
				content.setIconUrl(videoResponse.getThumb());
				content.setTitle(videoResponse.getTitle());
				content.setSource("https://jp.pornhub.com/embed/" + videoResponse.getVideoId());
				content.setPublishBy(publishBy);
				content.setPublishBy(publishAt);

				return content;
			}

			public void setPublishAt(String value) {
				this.setPublishAt(DateTime.parse(value));
			}

			public void setPublishAt(DateTime value) {
				final Period period = new Period(value, DateTime.now());

				final String timeAgo = DateUtils.of(period).add(Unit.YEARS, "year", "years").add(Unit.MONTHS, "month", "months").add(Unit.WEEKS, "week", "weeks").add(Unit.DAYS, "day", "days").add(Unit.HOURS, "hour", "hours").add(Unit.MINUTES, "minute", "minutes").add(Unit.SECONDS, "second", "seconds").print();
				final String time = timeAgo.isBlank() ? "today" : String.format("%s %s", timeAgo, "ago");

				this.publishAt = time;
			}
		}

		public String json() {
			try {
				return new ObjectMapper().writeValueAsString(this);
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return "";
		}

		public ResponseBodyContents map(SearchResponse searchResponse) {
			this.setContents(searchResponse.getVideos().stream().map(Content::map).toList());
			return this;
		}

		public ResponseBodyContents map(Article.Response response) {
			this.setContents(response.getArticles().stream().map(Content::map).toList());
			return this;
		}
	}

}
