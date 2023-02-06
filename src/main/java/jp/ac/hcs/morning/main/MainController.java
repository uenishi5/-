package jp.ac.hcs.morning.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;
import jp.ac.hcs.morning.chart.ChartEntity;
import jp.ac.hcs.morning.horoscope.HoroscopeEntity;
import jp.ac.hcs.morning.traffic.TrafficData;
import jp.ac.hcs.morning.traffic.TrafficEntity;
import jp.ac.hcs.morning.weather.WeatherEntity;
import jp.ac.hcs.morning.weather_alert.Weather_alertEntity;

/**
 * ログインに関する機能・画面を制御する
 */

@Controller
public class MainController {

	@Autowired
	private MainService mainService;

	/**
	 * メイン画面を表示する
	 *
	 * @param model
	 * @return ログイン画面
	 */
	@RequestMapping("/main")
	public String getMainData(Model model) {
		/** 天気データを格納 */
		final WeatherEntity weatherEntity = this.mainService.getWeatherData();
		model.addAttribute("WeatherEntity", weatherEntity);

		/** 占いデータを格納 */
		final HoroscopeEntity horoscopeEntity = this.mainService.getMainHoroscopeData();
		model.addAttribute("HoroscopeEntity", horoscopeEntity);

		/** 札幌の警報を取得 */
		final Weather_alertEntity weather_alertEntity = this.mainService.getMainWeather_alertData();
		model.addAttribute("Weather_alertEntity", weather_alertEntity);

		/** ビットコインチャートを取得 */
		final ChartEntity chartEntity = this.mainService.getMainChartData();
		model.addAttribute("ChartEntity", chartEntity);

		/** 札幌市のバス情報取得 */
		final TrafficEntity trafficEntity = this.mainService.getMainTrafficData();
		final TrafficData trafficData = trafficEntity.getTrafficflgList().get(0);
		model.addAttribute("flg", trafficData.isAlertflg());

		/** Jr情報取得 */
		final boolean jrflg = this.mainService.getMainJrData();
		model.addAttribute("jrflg", jrflg);

		return Mapping.RESOURCE_MAIN;
	}
}
