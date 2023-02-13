package jp.ac.hcs.mbraw.controller.weather_alert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;

/** 警報注意報のコントローラー */
@Controller
public class WeatherAlertController {

	@Autowired
	private WeatherAlertService weatherAlertService;

	@RequestMapping(Mapping.MAPPING_WEATHER_ALERT)
	public String getMainData(Model model) {
		final WeatherAlertEntity entity = this.weatherAlertService.getWeather_alertData();
		final WeatherAlertData data = entity.getWeather_alertnameList().get(0);
		final String name = data.getName();
		if (!(name.equals("発表なし"))) {
			final WeatherAlertData datelist = entity.getWeather_dateList().get(0);
			final int col1 = datelist.getColspan1();
			final int col2 = datelist.getColspan2();
			model.addAttribute("colspan1", col1);
			model.addAttribute("colspan2", col2);
		}

		if (!(entity.isError())) {
			model.addAttribute("flg", name.equals("発表なし"));
		}
		System.out.println(entity);

		model.addAttribute("Weather_alert", entity);
		return Mapping.RESOURCE_WEATHER_ALERT;
	}

}
