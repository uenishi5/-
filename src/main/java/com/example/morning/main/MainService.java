package com.example.morning.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.morning.chart.ChartData;
import com.example.morning.chart.ChartEntity;
import com.example.morning.horoscope.HoroscopeData;
import com.example.morning.horoscope.HoroscopeEntity;
import com.example.morning.traffic.TrafficData;
import com.example.morning.traffic.TrafficEntity;
import com.example.morning.weather.WeatherData;
import com.example.morning.weather.WeatherEntity;
import com.example.morning.weather_alert.Weather_alertData;
import com.example.morning.weather_alert.Weather_alertEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * メイン画面に必要な情報を取得するサービス
 */
@Service
public class MainService {
	/** エンドポイント */
	private static final String HOROSCOPE = "http://api.jugemkey.jp/api/horoscope/free/";
	private static final String SAPPORO = "https://weather.tsukumijima.net/api/forecast/city/016010";
	private static final String SAPPORO_BUS = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=16";
	/** 高速バス情報 */
	private static final String NORTH_SAPPORO_ISHIKARI = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=16";
	private static final String SOUTH_SAPPORO_NORTH_HIROSHIMA_CHITOSE_EBETSU = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=17";
	private static final String OTARU_SHINAI = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=12";
	private static final String OTARU_HOUMEN = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=13";
	private static final String YOICHI_SHAKOTAN = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=14";
	private static final String TAKIGAWA = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=11";
	private static final String IWAMIZAWA = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=10";
	private static final String BUSLIST[] = { NORTH_SAPPORO_ISHIKARI, SOUTH_SAPPORO_NORTH_HIROSHIMA_CHITOSE_EBETSU,
			OTARU_SHINAI, OTARU_HOUMEN, YOICHI_SHAKOTAN, TAKIGAWA, IWAMIZAWA };
	@Autowired
	private RestTemplate restTemplate;

	/** メイン画面用データ取得 */
	public WeatherEntity getWeatherData() {
		WeatherEntity entity = new WeatherEntity();
		String result = "";
		// APIへリクエスト送信
		URL url;
		try {
			url = new URL(SAPPORO);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String tmp = "";

			while ((tmp = in.readLine()) != null) {
				result += tmp;
			}

			// 結果をデータに変換

			entity = this.convert(result);
			in.close();
			connection.disconnect();
		} catch (Exception e) {
			WeatherData data = new WeatherData();
			data.setCatchflg(true);
			entity.getWeatherList().add(data);
		}
		return entity;
	}
// データをリストに格納するメソッド
	private WeatherEntity convert(String json) {

		ObjectMapper objectMapper = new ObjectMapper();
		WeatherData data = new WeatherData();
		WeatherEntity entity = new WeatherEntity();
		try {

			JsonNode weather = objectMapper.readValue(json, JsonNode.class);
			if (weather.get("forecasts").get(0).get("telop").asText() == "null") {
				data.setTelop("情報無し");
			} else {
				data.setTelop(weather.get("forecasts").get(0).get("telop").asText());
			}
			if (weather.get("forecasts").get(0).get("temperature").get("max")
					.get("celsius").asText() == "null") {
				data.setTemperature_max("--");
			} else {
				data.setTemperature_max(weather.get("forecasts").get(0).get("temperature").get("max")
						.get("celsius").asText());
			}
			if (weather.get("forecasts").get(0).get("temperature").get("min")
					.get("celsius").asText() == "null") {
				data.setTemperature_min("--");
			} else {
				data.setTemperature_min(weather.get("forecasts").get(0).get("temperature").get("min")
						.get("celsius").asText());
			}
			data.setSvg(weather.get("forecasts").get(0).get("image").get("url").asText());

			data.setPrefecture(weather.get("location").get("prefecture").asText());
			data.setCity(weather.get("location").get("city").asText());

			entity.getWeatherList().add(data);

		} catch (IOException e) {
			data.setTelop("");
			data.setTemperature_max("");
			data.setTemperature_min("");
			data.setSvg("");

			data.setPrefecture("");
			data.setCity("");
			entity.getWeatherList().add(data);
		}

		return entity;
	}

	/** メイン画面用占いデータ取得*/
	public HoroscopeEntity getMainHoroscopeData() {
		HoroscopeEntity entity = new HoroscopeEntity();
		String result = "";

		URL url;
		try {
			String date = simpleDateFormat();
			String horoscopeurl = HOROSCOPE + date;
			url = new URL(horoscopeurl);
			// APIへリクエスト送信
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String tmp = "";

			while ((tmp = in.readLine()) != null) {
				result += tmp;
			}

			// 結果をデータに変換

			entity = this.convert_top(result, date);
			in.close();
			connection.disconnect();
		} catch (IOException e) {
			HoroscopeData data = new HoroscopeData();
			data.setCatchflg(true);
			data.setTop("エラーが発生しました");
			data.setPhotopath("");
			entity.getHoroscopeList().add(data);

		}
		return entity;

	}
// データをリストに格納するメソッド
	private HoroscopeEntity convert_top(String json, String date) {
		ObjectMapper objectMapper = new ObjectMapper();

		HoroscopeEntity entity = new HoroscopeEntity();

		try {
			JsonNode horoscope = objectMapper.readValue(json, JsonNode.class);
			for (int idx = 0; idx < 12; idx++) {

				HoroscopeData data = new HoroscopeData();
				String horotitle = horoscope.get("horoscope").get(date).get(idx).get("sign").asText();
				int getrank = horoscope.get("horoscope").get(date).get(idx).get("rank").asInt();
				if (getrank == 1) {
					data.setTop(horoscope.get("horoscope").get(date).get(idx).get("sign").asText());
					String photopath = null;
					if (horotitle.equals("牡羊座")) {
						photopath = "img/ohitsuji.png";
					} else if (horotitle.equals("乙女座")) {
						photopath = "img/otome.png";
					} else if (horotitle.equals("天秤座")) {
						photopath = "img/tenbin.png";
					} else if (horotitle.equals("双子座")) {
						photopath = "img/futago.png";
					} else if (horotitle.equals("牡牛座")) {
						photopath = "img/oushi.png";
					} else if (horotitle.equals("獅子座")) {
						photopath = "img/shishi.png";
					} else if (horotitle.equals("蠍座")) {
						photopath = "img/sasori.png";
					} else if (horotitle.equals("蟹座")) {
						photopath = "img/kani.png";
					} else if (horotitle.equals("射手座")) {
						photopath = "img/ite.png";
					} else if (horotitle.equals("山羊座")) {
						photopath = "img/yagi.png";
					} else if (horotitle.equals("水瓶座")) {
						photopath = "img/mizugame.png";
					} else if (horotitle.equals("魚座")) {
						photopath = "img/uo.png";
					}
					data.setPhotopath(photopath);
					entity.getHoroscopeList().add(data);
					break;
				}
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return entity;
	}

	private String simpleDateFormat() {
		Calendar cl = Calendar.getInstance();

		//日付をyyyy/MM/ddの形で出力する
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		String str1 = sdf1.format(cl.getTime());
		return str1;
	}

	/** 警報・注意報取得  */
	public Weather_alertEntity getMainWeather_alertData() {
		Document document;
		Weather_alertEntity entity = new Weather_alertEntity();

		try {
			document = Jsoup.connect("https://typhoon.yahoo.co.jp/weather/jp/warn/1b/1100/").get();

			Elements getalert = document.select(".warnDetail_head");
			String[] alert = getalert.text().split(" ");
			if (alert[1].equals("発表なし")) {
				Weather_alertData data = new Weather_alertData();
				data.setName(alert[1]);
				entity.getWeather_alertnameList().add(data);
				data.setAlert_color("white");
			} else {
				int alertcount = alert.length;
				for (int idx = 1; idx < alertcount; idx++) {
					Weather_alertData data = new Weather_alertData();
					if ((alert[idx].matches(".*注意報"))) {
						data.setName(alert[idx].replace("注意報", ""));

						data.setAlert_color("yellow");
					} else if ((alert[idx].matches(".*警報"))) {
						data.setName(alert[idx].replace("警報", ""));

						data.setAlert_color("red");
					} else if ((alert[idx].matches(".*特別警報"))) {
						data.setName(alert[idx].replace("特別警報", ""));

						data.setAlert_color("black");
					}
					entity.getWeather_alertnameList().add(data);
				}
			}
		} catch (IOException e) {
			Weather_alertData data = new Weather_alertData();
			data.setCatchflg(true);
			data.setName("エラー");
			data.setAlert_color("red");
			entity.getWeather_alertnameList();
		}
		return entity;
	}

	/** ビットコインチャートデータの取得 */
	public ChartEntity getMainChartData() {
		ChartEntity entity = new ChartEntity();
		ChartData data = new ChartData();
		Document document;
		try {
			document = Jsoup.connect("https://bitflyer.com/ja-jp/bitcoin-chart").get();

			Elements elements = document.select(".p-currencyInfo__head");
			String[] chartlist = null;
			chartlist = elements.text().split(" ");
			String bitcoin = chartlist[0].replace("※", " ");
			String rate = chartlist[1];
			data.setBitcoin(bitcoin);
			data.setRate(rate);
			entity.getChartList().add(data);
		} catch (IOException e) {
			data.setCatchflg(true);
			data.setBitcoin("");
			data.setRate("");
			entity.getChartList().add(data);
		}
		return entity;
	}
/** 交通情報を取得*/
	public TrafficEntity getMainTrafficData() {
		TrafficEntity entity = new TrafficEntity();
		TrafficData data = new TrafficData();
		Document document;
		data.setAlertflg(false);
		try {
			for (int idx = 0; idx < 7; idx++) {
				document = Jsoup.connect(BUSLIST[idx]).get();
				Elements f16 = document.select(".F16");
				if (!(f16.text().equals("現在、運休の情報はありません。 道路状況等により遅延が発生する場合がありますので、 詳しくは各ターミナル・営業所にお問合せください。"))) {
					data.setAlertflg(true);
					break;
				}
			}
		} catch (IOException e) {
			data.setCatchflg(true);
			data.setAlertflg(false);
			entity.getTrafficList().add(data);
		}
		entity.getTrafficflgList().add(data);
		return entity;
	}
}