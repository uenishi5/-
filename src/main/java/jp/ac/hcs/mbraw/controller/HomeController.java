package jp.ac.hcs.mbraw.controller;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collections;
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

import com.google.api.services.youtube.model.SearchListResponse;
import com.ren130302.webapi.newsapi.NewsApiClient;
import com.ren130302.webapi.newsapi.response.Article;
import com.ren130302.webapi.pornhubapi.PornhubApiClient;
import com.ren130302.webapi.pornhubapi.response.SearchResponse;

import jp.ac.hcs.config.ApiKeyHolder;
import jp.ac.hcs.config.EntityHolder;
import jp.ac.hcs.config.Mapping;
import jp.ac.hcs.mbraw.DownloadHelper;
import jp.ac.hcs.mbraw.ResponseBodyContents;
import jp.ac.hcs.mbraw.YouTubeInstance;
import jp.ac.hcs.mbraw.controller.chart.ChartService;
import jp.ac.hcs.mbraw.controller.horoscope.HoroscopeEntity;
import jp.ac.hcs.mbraw.controller.horoscope.HoroscopeService;
import jp.ac.hcs.mbraw.controller.jr.JrData;
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
		HomeController.log.debug("GET {}", Mapping.MAPPING_ROOT);

		// 天気データを格納
		final WeatherEntity weatherEntity = new WeatherEntity();
		final WeatherData weatherData = this.weatherService.getWeatherData().getWeatherList().get(0);
		weatherEntity.getWeatherList().add(weatherData);
		model.addAttribute("WeatherEntity", weatherEntity);

		// 占いデータをHoroscopeServiceから取得して 占いの順位が良い1位のデータをHoroscopeEntityに格納する
		final HoroscopeEntity horoscopeEntity = new HoroscopeEntity();

		if (Objects.isNull(EntityHolder.HOROSCOPE_TODAY)) {
			EntityHolder.HOROSCOPE_TODAY = this.horoscopeService.getHoroscopeData().getHoroscopeList();
		}

		// final HoroscopeData horoscopeDataRank1 = HOROSCOPE_TODAY.get(0);
		// horoscopeEntity.getHoroscopeList().add(horoscopeDataRank1);
		// model.addAttribute("HoroscopeEntity", horoscopeEntity);

		horoscopeEntity.getHoroscopeList().addAll(EntityHolder.HOROSCOPE_TODAY);
		model.addAttribute("HoroscopeEntity", horoscopeEntity);

		// 札幌の警報を取得
		final WeatherAlertEntity weatherAlertEntity = new WeatherAlertEntity();
		final WeatherAlertData weatherAlertData = this.weatherAlertService.getWeatherAlertEntity(true).getWeather_alertnameList().get(0);
		weatherAlertEntity.getWeather_alertnameList().add(weatherAlertData);
		model.addAttribute("Weather_alertEntity", weatherAlertEntity);

		// ビットコインチャートを取得
		this.chartService.getChartData().addAttribute(model);

		// 札幌市のバス情報取得
		final TrafficData trafficData = this.trafficService.getMainTrafficData().getTrafficflgList().get(0);
		model.addAttribute("flg", trafficData.isAlertflg());

		// Jr情報取得
		final boolean jrflg = this.jrService.getallJrData().getJrList().stream().anyMatch(JrData::isAlert);
		model.addAttribute("jrflg", jrflg);

		model.asMap().forEach((k, v) -> HomeController.log.debug("key={}, value={}", k, v));

		return Mapping.RESOURCE_INDEX;
	}

	/**
	 * 非同期通信専用のメソッドです。 json形式のデータは、引数のformの中身によって変化します。
	 *
	 * @param pageable
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(Mapping.MAPPING_NEWSAPI)
	@ResponseBody
	public String postNewsApi(Pageable pageable, @Validated NewsForm form, BindingResult bindingResult) {
		HomeController.log.debug("POST {}", Mapping.MAPPING_NEWSAPI);
		HomeController.log.debug("Request params:{}", form);
		HomeController.log.debug("page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

		final ResponseBodyContents responseBodyContents = new ResponseBodyContents();

		// 引数 form の値が正常であれば分岐がスキップできるが
		// 何かしらの異常が存在する場合は、オブジェクト responseBodyContentsを
		// json形式に変換しレスポンスを返す
		if (bindingResult.hasErrors()) {
			responseBodyContents.setErrors(bindingResult.getAllErrors());
			return responseBodyContents.json();
		}

		// NewsApi問い合わせ
		// もし問い合わせに失敗した場合は、空の文字列を返す。
		try {
			Response<Article.Response> response = NewsApiClient.EVERYTHING.ready(this.holder.getNews(), b -> form.query(b).page(pageable.getPageNumber()).pageSize(pageable.getPageSize())).execute();
			HomeController.log.debug("{}", response.raw().request().url().toString());

			if (response.isSuccessful()) {
				final Article.Response articleResponse = Objects.nonNull(response.body()) ? response.body() : new Article.Response();
				return ResponseBodyContents.Utils.map(articleResponse).json();
			}
			else {
				HomeController.log.debug("{}", response.errorBody().string());
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			return ResponseBodyContents.JSON_EMPTY;
		}

		return responseBodyContents.json();
	}

	/**
	 * 非同期通信専用のメソッドです。 json形式のデータは、引数のformの中身によって変化します。
	 *
	 * @param pageable
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(Mapping.MAPPING_PORNHUBAPI)
	@ResponseBody
	public String postPornHubApi(Pageable pageable, @Validated PornhubForm form, BindingResult bindingResult) {
		HomeController.log.debug("POST {}", Mapping.MAPPING_PORNHUBAPI);
		HomeController.log.debug("Request params:{}", form);
		HomeController.log.debug("page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

		final ResponseBodyContents responseBodyContents = new ResponseBodyContents();

		// 引数 form の値が正常であれば分岐がスキップできるが
		// 何かしらの異常が存在する場合は、オブジェクト responseBodyContentsを
		// json形式に変換しレスポンスを返す
		if (bindingResult.hasErrors()) {
			responseBodyContents.setErrors(bindingResult.getAllErrors());
			return responseBodyContents.json();
		}

		// PornhubApi問い合わせ
		// もし問い合わせに失敗した場合は、空の文字列を返す。
		try {
			Response<SearchResponse> response = PornhubApiClient.SEARCH.ready(b -> form.query(b).page(pageable.getPageNumber())).execute();
			HomeController.log.debug("{}", response.raw().request().url().toString());

			if (response.isSuccessful()) {
				final SearchResponse searchResponse = Objects.nonNull(response.body()) ? response.body() : new SearchResponse();
				return ResponseBodyContents.Utils.map(searchResponse).json();
			}
			else {
				HomeController.log.debug("{}", response.errorBody().string());
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return responseBodyContents.json();
	}

	/**
	 * 非同期通信専用のメソッドです。 json形式のデータは、引数のformの中身によって変化します。
	 *
	 * @param pageable
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(Mapping.MAPPING_YOUTUBEAPI)
	@ResponseBody
	public String postYoutubeApI(Pageable pageable, @Validated YoutubeForm form, BindingResult bindingResult) {
		HomeController.log.debug("POST {}", Mapping.MAPPING_YOUTUBEAPI);
		HomeController.log.debug("Request params:{}", form);
		HomeController.log.debug("page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

		final ResponseBodyContents responseBodyContents = new ResponseBodyContents();

		// 引数 form の値が正常であれば分岐がスキップできるが
		// 何かしらの異常が存在する場合は、オブジェクト responseBodyContentsを
		// json形式に変換しレスポンスを返す
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

	/**
	 * 動画をダウンロードするためのメソッドです。
	 *
	 * @param videoId
	 * @param toMp3
	 * @return
	 */
	@GetMapping(Mapping.MAPPING_YOUTUBE_DL)
	public ResponseEntity<byte[]> postYoutubeDL(@Validated YoutubeDLForm form, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		final String extension = form.isToMp3() ? "mp3" : "mp4";
		final String filename = String.format("%s.%s", form.getVideoId(), extension);
		final Path path = FileSystems.getDefault().getPath(Paths.get(System.getProperty("user.home"), "downloads", filename).toAbsolutePath().toString());

		HomeController.log.debug("path={}", path);

		return DownloadHelper.execute(path, form.getUrl(), form.isToMp3());
	}

}
