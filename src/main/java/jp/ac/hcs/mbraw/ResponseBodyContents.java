package jp.ac.hcs.mbraw;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.ren130302.utils.DateUtils;
import com.ren130302.utils.DateUtils.Unit;
import com.ren130302.webapi.newsapi.response.Article;
import com.ren130302.webapi.pornhubapi.response.SearchResponse;
import com.ren130302.webapi.pornhubapi.response.VideoResponse;

import lombok.Data;
import lombok.NonNull;
import lombok.Value;

/**
 * API通信した後、必要な情報を格納を行いResponseBodyとしてレスポンスするためのクラスです。
 *
 *
 * @author s20203029
 */
@Data
public class ResponseBodyContents {

	public static final String JSON_EMPTY = "";

	/**
	 * 何かしらのエラーを格納するための変数
	 */
	private List<ObjectError> errors;

	/**
	 * API通信して格納するための変数
	 */
	private List<Content> contents;

	/**
	 * フロントサイドのcssに存在するitemクラスのためのクラス
	 *
	 * このクラスに宣言されている変数は必要な情報である。 そのため必ず値が代入されるべきである。
	 *
	 * @author s20203029
	 */
	@Value(staticConstructor = "create")
	public static class Content {

		private final @NonNull String originalUrl;
		private final @NonNull String iconUrl;
		private final @NonNull String title;
		private final @NonNull String source;
		private final @NonNull String publishBy;
		private final @NonNull String publishAt;

		public static String getTimeAgo(DateTime dateTime) {
			final Period period = new Period(dateTime, DateTime.now());

			final String timeAgo = DateUtils.of(period).add(Unit.YEARS, "year", "years").add(Unit.MONTHS, "month", "months").add(Unit.WEEKS, "week", "weeks").add(Unit.DAYS, "day", "days").add(Unit.HOURS, "hour", "hours").add(Unit.MINUTES, "minute", "minutes").add(Unit.SECONDS, "second", "seconds").print();
			final String output = timeAgo.isBlank() ? "today" : String.format("%s %s", timeAgo, "ago");
			return output;
		}
	}

	public String json() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			e.printStackTrace();
			return JSON_EMPTY;
		}
	}

	public static class Utils {
		public static Content map(SearchResult searchResult) {
			final ResourceId resourceId = searchResult.getId();
			final SearchResultSnippet searchResultSnippet = searchResult.getSnippet();
			final Thumbnail thumbnail = searchResultSnippet.getThumbnails().getHigh();

			final String originalUrl = String.format("https://www.youtube.com/watch?v=%s", resourceId.getVideoId());
			final String iconUrl = thumbnail.getUrl();
			final String title = searchResultSnippet.getTitle();
			final String source = String.format("https://www.youtube.com/embed/%s", resourceId.getVideoId());
			final String publishBy = searchResultSnippet.getChannelTitle();
			final String publishAt = searchResultSnippet.getPublishedAt().toString();

			return Content.create(originalUrl, iconUrl, title, source, publishBy, publishAt);
		}

		public static Content map(Article article) {
			final String originalUrl = article.getUrl();
			final String iconUrl = article.getUrlToImage();
			final String title = article.getTitle();
			final String source = article.getUrl();
			final String publishBy = article.getAuthor();
			final String publishAt = article.getPublishedAt();

			return Content.create(originalUrl, iconUrl, title, source, publishBy, publishAt);
		}

		public static Content map(VideoResponse videoResponse) {
			final String originalUrl = videoResponse.getUrl();
			final String iconUrl = videoResponse.getThumb();
			final String title = videoResponse.getTitle();
			final String source = String.format("https://jp.pornhub.com/embed/%s", videoResponse.getVideoId());
			final String publishBy = convertDate(videoResponse.getPublishDate());
			final String publishAt = videoResponse.getPornstars().isEmpty() ? "" : videoResponse.getPornstars().get(0).getPornstarName();

			return Content.create(originalUrl, iconUrl, title, source, publishBy, publishAt);
		}

		public static ResponseBodyContents map(SearchListResponse searchListResponse) {
			return map(searchListResponse.getItems().stream().map(Utils::map).toList());
		}

		public static ResponseBodyContents map(SearchResponse searchResponse) {
			return map(searchResponse.getVideos().stream().map(Utils::map).toList());
		}

		public static ResponseBodyContents map(Article.Response response) {
			return map(response.getArticles().stream().map(Utils::map).toList());
		}

		public static ResponseBodyContents map(List<Content> contents) {
			final ResponseBodyContents responseBodyContents = new ResponseBodyContents();

			responseBodyContents.setContents(contents);

			return responseBodyContents;
		}

		/**
		 * Parses text from the beginning of the given string to produce a date.The
		 * method may not use the entire text of the given string.
		 *
		 * See the parse(String, ParsePosition) method for more informationon date
		 * parsing.
		 *
		 * @param value
		 * @return If catched exception,Return String length 0;
		 */
		private static String convertDate(String value) {

			try {
				return new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss").parse(value).toInstant().toString();
			}
			catch (ParseException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

}