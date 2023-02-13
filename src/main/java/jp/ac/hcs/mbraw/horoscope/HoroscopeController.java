package jp.ac.hcs.mbraw.horoscope;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;
import jp.ac.hcs.mbraw.controller.HomeController;

@Controller
public class HoroscopeController {

	@Autowired
	private HoroscopeService horoscopeService;

	/** 占い画面に遷移するコントローラー */
	@RequestMapping("/Horoscope")
	public String getHoroscopeData(Model model) {
		/** 全ての星座情報をリストで、昇順に格納する。 */
		final HoroscopeEntity entity = new HoroscopeEntity();

		if (Objects.isNull(HomeController.HOROSCOPE_TODAY)) {
			HomeController.HOROSCOPE_TODAY = this.horoscopeService.getHoroscopeData().getHoroscopeList();
		}

		entity.getHoroscopeList().addAll(HomeController.HOROSCOPE_TODAY);

		model.addAttribute("HoroscopeEntity", entity);

		return Mapping.RESOURCE_HOROSCOPE;
	}
}
