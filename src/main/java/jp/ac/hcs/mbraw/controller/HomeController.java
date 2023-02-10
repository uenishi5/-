package jp.ac.hcs.mbraw.controller;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.ren130302.utils.DateUtils;
import com.ren130302.utils.DateUtils.Unit;
import com.ren130302.webapi.newsapi.NewsApiClient;
import com.ren130302.webapi.newsapi.response.Article;
import com.ren130302.webapi.pornhubapi.PornhubApiClient;
import com.ren130302.webapi.pornhubapi.response.SearchResponse;
import com.ren130302.webapi.pornhubapi.response.VideoResponse;

import jp.ac.hcs.config.ApiKeyHolder;
import jp.ac.hcs.config.Mapping;
import jp.ac.hcs.mbraw.chart.ChartData;
import jp.ac.hcs.mbraw.chart.ChartEntity;
import jp.ac.hcs.mbraw.chart.ChartService;
import jp.ac.hcs.mbraw.horoscope.HoroscopeData;
import jp.ac.hcs.mbraw.horoscope.HoroscopeEntity;
import jp.ac.hcs.mbraw.horoscope.HoroscopeService;
import jp.ac.hcs.mbraw.jr.JrService;
import jp.ac.hcs.mbraw.traffic.TrafficData;
import jp.ac.hcs.mbraw.traffic.TrafficService;
import jp.ac.hcs.mbraw.weather.WeatherData;
import jp.ac.hcs.mbraw.weather.WeatherEntity;
import jp.ac.hcs.mbraw.weather.WeatherService;
import jp.ac.hcs.mbraw.weather_alert.Weather_alertData;
import jp.ac.hcs.mbraw.weather_alert.Weather_alertEntity;
import jp.ac.hcs.mbraw.weather_alert.Weather_alertService;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

/**
 * index.htmlを制御しているクラスです。
 * 基本的にPOSTメソッドで通信が行われてます。非同期通信のレスポンスボディーを返す専用のクラスが存在しています。
 *
 * @author s20203029
 *
 */
@Slf4j
@Controller
public class HomeController {

	private static final String JSON_EMPTY = "";

	@Autowired
	private ApiKeyHolder holder;

	@Autowired
	private HoroscopeService horoscopeService;

	@Autowired
	private WeatherService weatherService;

	@Autowired
	private Weather_alertService weather_alertService;

	@Autowired
	private ChartService chartService;

	@Autowired
	private TrafficService trafficService;

	@Autowired
	private JrService jrService;

	@GetMapping(Mapping.MAPPING_ROOT)
	public String getHome(Principal principal, Model model, Pageable pageable) {
		log.debug("GET {}", Mapping.MAPPING_ROOT);

		/** 天気データを格納 */
		final WeatherEntity weatherEntity = new WeatherEntity();
		final WeatherData weatherData = this.weatherService.getWeatherData().getWeatherList().get(0);
		weatherEntity.getWeatherList().add(weatherData);
		model.addAttribute("WeatherEntity", weatherEntity);

		/**
		 * 占いデータをHoroscopeServiceから取得して 占いの順位が良い1位のデータをHoroscopeEntityに格納する
		 */
		final HoroscopeEntity horoscopeEntity = new HoroscopeEntity();
		final HoroscopeData horoscopeDataRank1 = this.horoscopeService.getHoroscopeData().getHoroscopeList().get(0);
		horoscopeEntity.getHoroscopeList().add(horoscopeDataRank1);
		model.addAttribute("HoroscopeEntity", horoscopeEntity);

		/** 札幌の警報を取得 */
		final Weather_alertEntity weather_alertEntity = new Weather_alertEntity();
		final Weather_alertData weather_alertData = this.weather_alertService.getMainWeather_alertData().getWeather_alertnameList().get(0);
		weather_alertEntity.getWeather_alertnameList().add(weather_alertData);
		model.addAttribute("Weather_alertEntity", weather_alertEntity);

		/** ビットコインチャートを取得 */
		final ChartEntity chartEntity = new ChartEntity();
		final ChartData chartData = this.chartService.getChartData().getChartList().get(0);
		chartEntity.getChartList().add(chartData);
		model.addAttribute("ChartEntity", chartEntity);

		/** 札幌市のバス情報取得 */
		final TrafficData trafficData = this.trafficService.getMainTrafficData().getTrafficflgList().get(0);
		model.addAttribute("flg", trafficData.isAlertflg());

		/** Jr情報取得 */
		final boolean jrflg = this.jrService.getMainJrFlg();
		model.addAttribute("jrflg", jrflg);

		return Mapping.RESOURCE_INDEX;
	}

	@PostMapping(Mapping.MAPPING_NEWSAPI)
	@ResponseBody
	public String postNewsApi(Pageable pageable, @Validated NewsForm form, BindingResult bindingResult) {
		log.debug("POST {}", Mapping.MAPPING_NEWSAPI);
		log.debug("Request params:{}", form);
		log.debug("page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

		final ResponseBodyContents responseBodyContents = new ResponseBodyContents();

		if (bindingResult.hasErrors()) {
			responseBodyContents.setErrors(bindingResult.getAllErrors());
			return responseBodyContents.json();
		}

		// NewsApi問い合わせ
		try {
			Response<Article.Response> response = NewsApiClient.EVERYTHING.ready(this.holder.getNews(), b -> form.query(b).page(pageable.getPageNumber()).pageSize(pageable.getPageSize())).execute();
			log.debug("{}", response.raw().request().url().toString());

			if (response.isSuccessful()) {
				final Article.Response articleResponse = Objects.nonNull(response.body()) ? response.body() : new Article.Response();
				return ResponseBodyContents.Utils.map(articleResponse).json();
			}
			else {
				log.debug("{}", response.errorBody().string());
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return JSON_EMPTY;
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
			responseBodyContents.setErrors(bindingResult.getAllErrors());
			return responseBodyContents.json();
		}

		// PornhubApi問い合わせ
		try {
			Response<SearchResponse> response = PornhubApiClient.SEARCH.ready(b -> form.query(b).page(pageable.getPageNumber())).execute();
			log.debug("{}", response.raw().request().url().toString());

			if (response.isSuccessful()) {
				final SearchResponse searchResponse = Objects.nonNull(response.body()) ? response.body() : new SearchResponse();
				return ResponseBodyContents.Utils.map(searchResponse).json();
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
			responseBodyContents.setErrors(bindingResult.getAllErrors());
			return responseBodyContents.json();
		}

		try {
			final SearchListResponse searchListResponse = YouTubeInstance.singleton().search().list(Collections.singletonList("id,snippet")).setKey(this.holder.getYoutube()).setQ(form.getQ()).setType(Collections.singletonList("video")).setOrder(EnumYoutubeSort.DATE.name().toLowerCase()).setFields("items(id/kind, id/videoId, snippet/title, snippet/publishedAt, snippet/thumbnails/high/url, snippet/channelTitle)").setMaxResults((long) pageable.getPageSize()).execute();
			return ResponseBodyContents.Utils.map(searchListResponse).json();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return responseBodyContents.json();
	}

	@GetMapping(Mapping.MAPPING_YOUTUBE_DL)
	public ResponseEntity<byte[]> postYoutubeDL(String videoId, boolean toMp3) {

		if (videoId.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		// videoIdが存在するかリクエストをする
		// エラーが起きた場合、正常なリクエストでない、またはAPIサーバが落ちている可能性がある
		// いずれにせよサーバ側でエラーが起きたことを通知するため、レスポンス(500エラー)を返す
		VideoListResponse videoListResponse = null;
		try {
			videoListResponse = YouTubeInstance.singleton().videos().list(Collections.singletonList("id,snippet")).setKey(this.holder.getYoutube()).setId(Arrays.asList(videoId)).execute();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}

		if (Objects.isNull(videoListResponse)) {
			return ResponseEntity.internalServerError().build();
		}

		// リクエストおよびレスポンスが正常に行われたため次の段階へすすむ
		// クライアントから正常なリクエストかどうかを確認する機構で
		// 受け取ったレスポンスの中身にあるプロパティ名"Items"が空でなければ正常とする
		// 空である場合は不具合として扱いレスポンス(400エラー)を返す
		final List<Video> items = videoListResponse.getItems();

		// 一件でも掛かればヨシ！
		if (items.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		// 動画の情報
		final Video video = videoListResponse.getItems().get(0);
		final String url = String.format("https://www.youtube.com/watch?v=%s", video.getId());
		final String extension = toMp3 ? "mp3" : "mp4";
		final String filename = String.format("%s.%s", video.getSnippet().getTitle(), extension);
		final Path path = FileSystems.getDefault().getPath(Paths.get(System.getProperty("user.home"), "downloads", filename).toAbsolutePath().toString());

		log.debug("path={}", path);

		return DownloadHelper.execute(path, url, toMp3);
	}

	public static class YouTubeInstance {
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

	/**
	 * API通信した後、必要な情報を格納を行いResponseBodyとしてレスポンスするためのクラスです。
	 *
	 *
	 * @author s20203029
	 */
	@Data
	public static class ResponseBodyContents {

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

}
