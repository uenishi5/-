package jp.ac.hcs.mbraw.controller.weather;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WeatherEntity {
	/** 天気情報リスト */
	private List<WeatherData> weatherList = new ArrayList<>();

	public static WeatherEntity error() {
		final WeatherEntity weatherEntity = new WeatherEntity();
		return weatherEntity;
	}
}
