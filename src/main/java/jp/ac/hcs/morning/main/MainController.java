package jp.ac.hcs.morning.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;
import jp.ac.hcs.morning.chart.ChartData;
import jp.ac.hcs.morning.chart.ChartEntity;
import jp.ac.hcs.morning.chart.ChartService;
import jp.ac.hcs.morning.horoscope.HoroscopeData;
import jp.ac.hcs.morning.horoscope.HoroscopeEntity;
import jp.ac.hcs.morning.horoscope.HoroscopeService;
import jp.ac.hcs.morning.jr.JrService;
import jp.ac.hcs.morning.traffic.TrafficData;
import jp.ac.hcs.morning.traffic.TrafficService;
import jp.ac.hcs.morning.weather.WeatherData;
import jp.ac.hcs.morning.weather.WeatherEntity;
import jp.ac.hcs.morning.weather.WeatherService;
import jp.ac.hcs.morning.weather_alert.Weather_alertData;
import jp.ac.hcs.morning.weather_alert.Weather_alertEntity;
import jp.ac.hcs.morning.weather_alert.Weather_alertService;

/**
 * ログインに関する機能・画面を制御する
 */

@Controller
public class MainController {

	@Autowired
	private MainService mainService;
	
	@Autowired
	private HoroscopeService horoscopeService;
	
	@Autowired
	private WeatherService weatherService;
	
	@Autowired
	private Weather_alertService weather_alertService;
	
	@Autowired
	private ChartService chartService;
	
	@Autowired
	private TrafficService trafficService;
	
	@Autowired
	private JrService jrService;

	/**
	 * メイン画面を表示する
	 *
	 * @param model
	 * @return ログイン画面
	 */
	@RequestMapping("/main")
	public String getMainData(Model model) {
		/** 天気データを格納 */
		final WeatherEntity weatherEntity = new WeatherEntity();
		final WeatherData weatherData = this.weatherService.getWeatherData().getWeatherList().get(0);
		weatherEntity.getWeatherList().add(weatherData);
		model.addAttribute("WeatherEntity", weatherEntity);

		/** 
		 * 占いデータをHoroscopeServiceから取得して
		 * 占いの順位が良い1位のデータをHoroscopeEntityに格納する
		 */
		final HoroscopeEntity horoscopeEntity = new HoroscopeEntity();
		final HoroscopeData horoscopeDataRank1 = this.horoscopeService.getHoroscopeData().getHoroscopeList().get(0);
		horoscopeEntity.getHoroscopeList().add(horoscopeDataRank1);
		model.addAttribute("HoroscopeEntity", horoscopeEntity);

		/** 札幌の警報を取得 */
		final Weather_alertEntity weather_alertEntity = new Weather_alertEntity();
		final Weather_alertData weather_alertData = this.weather_alertService.getMainWeather_alertData().getWeather_alertnameList().get(0);
		weather_alertEntity.getWeather_alertnameList().add(weather_alertData);
		model.addAttribute("Weather_alertEntity", weather_alertEntity);

		/** ビットコインチャートを取得 */
		final ChartEntity chartEntity = new ChartEntity();
		final ChartData chartData = this.chartService.getChartData().getChartList().get(0);
		chartEntity.getChartList().add(chartData);
		model.addAttribute("ChartEntity", chartEntity);

		/** 札幌市のバス情報取得 */
		final TrafficData trafficData = this.trafficService.getMainTrafficData().getTrafficflgList().get(0);
		model.addAttribute("flg", trafficData.isAlertflg());

		/** Jr情報取得 */
		final boolean jrflg = this.jrService.getMainJrFlg();
		model.addAttribute("jrflg", jrflg);

		return Mapping.RESOURCE_MAIN;
	}
}
