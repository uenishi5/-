package jp.ac.hcs.mbraw.weather_alert;

import java.util.ArrayList;
import java.util.List;

import jp.ac.hcs.mbraw.weather_alert.Weather_alertData.AlertColor;
import lombok.Data;

@Data
public class Weather_alertEntity {
	private List<Weather_alertData> weather_alertnameList = new ArrayList<>();

	private List<Weather_alertData> weather_alertList = new ArrayList<>();

	private List<Weather_alertData> weather_alert2List = new ArrayList<>();

	private List<Weather_alertData> weather_dateList = new ArrayList<>();

	private boolean error = false;

	public static Weather_alertEntity error() {
		final Weather_alertEntity weatherAlertEntity = new Weather_alertEntity();
		final Weather_alertData weatherAlertData = new Weather_alertData();

		weatherAlertEntity.setError(true);
		weatherAlertData.setCatchflg(true);
		weatherAlertData.setName("エラー");
		weatherAlertData.setAlert_color(AlertColor.RED);

		weatherAlertEntity.getWeather_dateList().add(weatherAlertData);

		return weatherAlertEntity;
	}
}
