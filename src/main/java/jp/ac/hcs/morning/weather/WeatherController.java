package jp.ac.hcs.morning.weather;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;

@Controller
public class WeatherController {
	

	/** メイン画面に表示するための天気データ（札幌固定)*/
	@RequestMapping("/Weather")
	public static String getWeatherData(Model model) {
		WeatherEntity entity = new WeatherEntity();
		WeatherService service = new WeatherService();
		// 結果を取得
		
		entity = service.getWeatherData();
		model.addAttribute("WeatherEntity", entity);
		return Mapping.RESOURCE_WEATHER;
	}
}
