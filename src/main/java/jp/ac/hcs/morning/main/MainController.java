package jp.ac.hcs.morning.main;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

	/**
	 * メイン画面を表示する
	 * @param model
	 * @return ログイン画面
	 * @throws IOException 
	 */
	@RequestMapping("/main")
	public String getMainData(Model model) {
		/** 天気データを格納 */
		MainService mainService = new MainService();
		WeatherEntity weatherEntity = new WeatherEntity();
		weatherEntity = mainService.getWeatherData();
		model.addAttribute("WeatherEntity", weatherEntity);
		/** 占いデータを格納 */
		HoroscopeEntity horoscopeEntity  = new HoroscopeEntity();
		horoscopeEntity = mainService.getMainHoroscopeData();
		model.addAttribute("HoroscopeEntity",horoscopeEntity);
		/** 札幌の警報を取得*/
		Weather_alertEntity weather_alertEntity = new Weather_alertEntity();
		weather_alertEntity = mainService.getMainWeather_alertData();
		model.addAttribute("Weather_alertEntity",weather_alertEntity);
		/** ビットコインチャートを取得 */
		ChartEntity chartEntity = new ChartEntity();
		chartEntity = mainService.getMainChartData();
		model.addAttribute("ChartEntity",chartEntity);
		/** 札幌市のバス情報取得 */
		TrafficEntity trafficEntity = new TrafficEntity();
		trafficEntity = mainService.getMainTrafficData();
		TrafficData a = trafficEntity.getTrafficflgList().get(0);
		boolean alertflg = a.isAlertflg();
		if(alertflg) {
			model.addAttribute("flg", true);
		}else {
			model.addAttribute("flg", false);
		}
		/** Jr情報取得*/
		boolean jrflg = mainService.getMainJrData();
		if(jrflg) {
			model.addAttribute("jrflg",true);
		}else {
			model.addAttribute("jrflg",false);
		}
		return "morning/main";
	}
}
