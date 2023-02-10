package jp.ac.hcs.mbraw.weather;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.hcs.mbraw.HttpConnectUtils;

/** 天気情報を取得するクラス */
@Service
public class WeatherService {
	/** エンドポイント */
	private static final String WEATHERURL = "https://weather.tsukumijima.net/api/forecast/city/016010";

	/** 天気情報を取得するメソッド */
	public WeatherEntity getWeatherData() {
		final String data = HttpConnectUtils.connectAndGetText(WEATHERURL);
		final WeatherEntity entity = data.isEmpty() ? WeatherEntity.error() : this.convert(data);

		return entity;
	}

	/** データをリストに格納するメソッド */
	private WeatherEntity convert(String json) {
		final ObjectMapper objectMapper = new ObjectMapper();

		JsonNode weatherNode = null;

		try {
			weatherNode = objectMapper.readValue(json, JsonNode.class);
		}
		catch (IOException e) {
			e.printStackTrace();
			return WeatherEntity.error();
		}

		final WeatherEntity entity = new WeatherEntity();

		for (int idx = 0; idx < 3; idx++) {
			final WeatherData data = new WeatherData();
			final JsonNode forecasts = weatherNode.get("forecasts");
			final JsonNode forecast = forecasts.get(idx);
			final JsonNode detail = forecast.get("detail");
			final JsonNode chanceOfRain = forecast.get("chanceOfRain");
			final JsonNode weather = detail.get("weather");
			final JsonNode location = weatherNode.get("location");
			final JsonNode temperature = forecast.get("temperature");

			data.setDate(forecast.get("date").asText());
			data.setDateLabel(forecast.get("dateLabel").asText());
			data.setTelop(forecast.get("telop").asText());
			data.setWeather(getText(weather.asText(), "null", "情報なし"));
			data.setWave(detail.get("wave").asText());
			data.setWind(detail.get("wind").asText());
			data.setTemperature_max(getText(temperature.get("max").get("celsius").asText(), "null", "--"));
			data.setTemperature_min(getText(temperature.get("min").get("celsius").asText(), "null", "--"));
			data.setChanceOfRain_T00_06(getText(chanceOfRain.get("T00_06").asText(), "null", "--"));
			data.setChanceOfRain_T06_12(getText(chanceOfRain.get("T06_12").asText(), "null", "--"));
			data.setChanceOfRain_T12_18(getText(chanceOfRain.get("T12_18").asText(), "null", "--"));
			data.setChanceOfRain_T18_24(getText(chanceOfRain.get("T18_24").asText(), "null", "--"));
			data.setArea(location.get("area").asText());
			data.setPrefecture(location.get("prefecture").asText());
			data.setCity(location.get("city").asText());
			data.setSvg(forecast.get("image").get("url").asText());

			entity.getWeatherList().add(data);

		}

		return entity;
	}

	public static String getText(String text, String equal, String error) {
		return text.equals(equal) ? error : text;
	}
}
