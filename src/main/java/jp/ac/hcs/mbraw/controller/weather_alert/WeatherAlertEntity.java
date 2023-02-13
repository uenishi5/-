package jp.ac.hcs.mbraw.controller.weather_alert;

import java.util.ArrayList;
import java.util.List;

import jp.ac.hcs.mbraw.controller.weather_alert.WeatherAlertData.AlertColor;
import lombok.Data;

@Data
public class WeatherAlertEntity {
	private List<WeatherAlertData> weather_alertnameList = new ArrayList<>();

	private List<WeatherAlertData> weather_alertList = new ArrayList<>();

	private List<WeatherAlertData> weather_alert2List = new ArrayList<>();

	private List<WeatherAlertData> weather_dateList = new ArrayList<>();

	private boolean error = false;

	public static WeatherAlertEntity error() {
		final WeatherAlertEntity weatherAlertEntity = new WeatherAlertEntity();
		final WeatherAlertData weatherAlertData = new WeatherAlertData();

		weatherAlertEntity.setError(true);
		weatherAlertData.setCatchflg(true);
		weatherAlertData.setName("エラー");
		weatherAlertData.setAlert_color(AlertColor.RED);

		weatherAlertEntity.getWeather_dateList().add(weatherAlertData);

		return weatherAlertEntity;
	}
}
