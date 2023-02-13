package jp.ac.hcs.mbraw.controller.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;

@Controller
public class WeatherController {

	@Autowired
	private WeatherService weatherService;

	/** メイン画面に表示するための天気データ（札幌固定) */
	@RequestMapping(Mapping.MAPPING_WEATHER)
	public String getWeatherData(Model model) {
		final WeatherEntity entity = this.weatherService.getWeatherData();

		model.addAttribute("WeatherEntity", entity);

		return Mapping.RESOURCE_WEATHER;
	}
}
