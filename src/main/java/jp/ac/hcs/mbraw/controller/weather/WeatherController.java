package jp.ac.hcs.mbraw.controller.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.ac.hcs.config.Mapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WeatherController {

	@Autowired
	private WeatherService weatherService;

	/** メイン画面に表示するための天気データ（札幌固定) */
	@GetMapping(Mapping.MAPPING_WEATHER)
	public String getWeatherData(Model model) {
		log.debug("GET {}", Mapping.MAPPING_WEATHER);

		final WeatherEntity entity = this.weatherService.getWeatherData();

		model.addAttribute("WeatherEntity", entity);

		model.asMap().forEach((k, v) -> log.debug("key={}, value={}", k, v));

		return Mapping.RESOURCE_WEATHER;
	}
}
