package com.example.morning.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// 天気情報を取得するクラス
@Service
public class WeatherService {
	/** エンドポイント */
	private static final String WEATHERURL = "https://weather.tsukumijima.net/api/forecast/city/016010";

	// 天気情報を取得するメソッド
	public WeatherEntity getWeatherData() {
		WeatherEntity entity = new WeatherEntity();
		String result = "";
		URL url;
		try {
			url = new URL(WEATHERURL);
			// APIへリクエスト送信
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String tmp = "";

			while ((tmp = in.readLine()) != null) {
				result += tmp;
			}

			// 結果をデータに変換
			System.out.println("ok");
			entity = this.convert(result);
			System.out.println(entity);
			in.close();
			connection.disconnect();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック

		}

		return entity;
	}

	// データをリストに格納するメソッド
	private WeatherEntity convert(String json) {
		ObjectMapper objectMapper = new ObjectMapper();

		WeatherEntity entity = new WeatherEntity();
		try {
			JsonNode weather = objectMapper.readValue(json, JsonNode.class);
			for (int idx = 0; idx < 3; idx++) {

				WeatherData data = new WeatherData();
				data.setDate(weather.get("forecasts").get(idx).get("date").asText());
				data.setDateLabel(weather.get("forecasts").get(idx).get("dateLabel").asText());
				data.setTelop(weather.get("forecasts").get(idx).get("telop").asText());
				if (weather.get("forecasts").get(idx).get("detail").get("weather").asText() == "null") {
					data.setWeather("情報無し");
				} else {
					data.setWeather(weather.get("forecasts").get(idx).get("detail").get("weather").asText());
				}
				data.setWave(weather.get("forecasts").get(idx).get("detail").get("wave").asText());
				data.setWind(weather.get("forecasts").get(idx).get("detail").get("wind").asText());
				if (weather.get("forecasts").get(idx).get("temperature").get("max")
						.get("celsius").asText() == "null") {
					data.setTemperature_max("--");
				} else {
					data.setTemperature_max(weather.get("forecasts").get(idx).get("temperature").get("max")
							.get("celsius").asText());
				}
				if (weather.get("forecasts").get(idx).get("temperature").get("min")
						.get("celsius").asText() == "null") {
					data.setTemperature_min("--");
				} else {
					data.setTemperature_min(weather.get("forecasts").get(idx).get("temperature").get("min")
							.get("celsius").asText());
				}
				data.setChanceOfRain_T00_06(
						weather.get("forecasts").get(idx).get("chanceOfRain").get("T00_06").asText());
				data.setChanceOfRain_T06_12(
						weather.get("forecasts").get(idx).get("chanceOfRain").get("T06_12").asText());
				if (weather.get("forecasts").get(idx).get("chanceOfRain").get("T12_18").asText() == "null") {
					data.setChanceOfRain_T12_18("--");
				} else {
					data.setChanceOfRain_T12_18(
							weather.get("forecasts").get(idx).get("chanceOfRain").get("T12_18").asText());
				}
				data.setChanceOfRain_T18_24(
						weather.get("forecasts").get(idx).get("chanceOfRain").get("T18_24").asText());
				data.setArea(weather.get("location").get("area").asText());
				data.setPrefecture(weather.get("location").get("prefecture").asText());
				data.setCity(weather.get("location").get("city").asText());
				data.setSvg(weather.get("forecasts").get(idx).get("image").get("url").asText());

				entity.getWeatherList().add(data);

			}
		} catch (IOException e) {

		}
		return entity;
	}
}
