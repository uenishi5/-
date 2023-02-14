package jp.ac.hcs.mbraw.controller;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.ren130302.webapi.newsapi.NewsApiClient;
import com.ren130302.webapi.newsapi.response.Article;
import com.ren130302.webapi.pornhubapi.PornhubApiClient;
import com.ren130302.webapi.pornhubapi.response.SearchResponse;

import jp.ac.hcs.config.ApiKeyHolder;
import jp.ac.hcs.config.EntityHolder;
import jp.ac.hcs.config.Mapping;
import jp.ac.hcs.mbraw.DownloadHelper;
import jp.ac.hcs.mbraw.ResponseBodyContents;
import jp.ac.hcs.mbraw.controller.chart.ChartData;
import jp.ac.hcs.mbraw.controller.chart.ChartEntity;
import jp.ac.hcs.mbraw.controller.chart.ChartService;
import jp.ac.hcs.mbraw.controller.horoscope.HoroscopeEntity;
import jp.ac.hcs.mbraw.controller.horoscope.HoroscopeService;
import jp.ac.hcs.mbraw.controller.jr.JrService;
import jp.ac.hcs.mbraw.controller.traffic.TrafficData;
import jp.ac.hcs.mbraw.controller.traffic.TrafficService;
import jp.ac.hcs.mbraw.controller.weather.WeatherData;
import jp.ac.hcs.mbraw.controller.weather.WeatherEntity;
import jp.ac.hcs.mbraw.controller.weather.WeatherService;
import jp.ac.hcs.mbraw.controller.weather_alert.WeatherAlertData;
import jp.ac.hcs.mbraw.controller.weather_alert.WeatherAlertEntity;
import jp.ac.hcs.mbraw.controller.weather_alert.WeatherAlertService;
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

	@Autowired
	private ApiKeyHolder holder;

	@Autowired
	private HoroscopeService horoscopeService;

	@Autowired
	private WeatherService weatherService;

	@Autowired
	private WeatherAlertService weatherAlertService;

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

		if (Objects.isNull(EntityHolder.HOROSCOPE_TODAY)) {
			EntityHolder.HOROSCOPE_TODAY = this.horoscopeService.getHoroscopeData().getHoroscopeList();
		}

		// final HoroscopeData horoscopeDataRank1 = HOROSCOPE_TODAY.get(0);
		// horoscopeEntity.getHoroscopeList().add(horoscopeDataRank1);
		// model.addAttribute("HoroscopeEntity", horoscopeEntity);

		horoscopeEntity.getHoroscopeList().addAll(EntityHolder.HOROSCOPE_TODAY);
		model.addAttribute("HoroscopeEntity", horoscopeEntity);

		/** 札幌の警報を取得 */
		final WeatherAlertEntity weatherAlertEntity = new WeatherAlertEntity();
		final WeatherAlertData weatherAlertData = this.weatherAlertService.getMainWeather_alertData().getWeather_alertnameList().get(0);
		weatherAlertEntity.getWeather_alertnameList().add(weatherAlertData);
		model.addAttribute("Weather_alertEntity", weatherAlertEntity);

		/** ビットコインチャートを取得 */
		final ChartEntity chartEntity = new ChartEntity();
		final ChartData chartData = this.chartService.getChartData().getChartList().get(0);
		chartEntity.getChartList().add(chartData);
		model.addAttribute("ChartEntity", chartEntity);

		/** 札幌市のバス情報取得 */
		final TrafficData trafficData = this.trafficService.getMainTrafficData().getTrafficflgList().get(0);
		model.addAttribute("flg", trafficData.isAlertflg());

		/** Jr情報取得 */
		final boolean jrflg = this.jrService.getallJrData().getJrList().stream().anyMatch(data -> data.isAlert());
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
			return ResponseBodyContents.JSON_EMPTY;
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

}
