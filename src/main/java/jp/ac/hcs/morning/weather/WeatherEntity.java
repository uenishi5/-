package jp.ac.hcs.morning.weather;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WeatherEntity {
	/** 天気情報リスト */
	private List<WeatherData> weatherList = new ArrayList<>();
}
