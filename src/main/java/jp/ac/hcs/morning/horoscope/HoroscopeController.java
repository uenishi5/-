package jp.ac.hcs.morning.horoscope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;

@Controller
public class HoroscopeController {

	@Autowired
	private HoroscopeService horoscopeService;

	/** 占い画面に遷移するコントローラー */
	@RequestMapping("/Horoscope")
	public String getHoroscopeData(Model model) {
		/** 全ての星座情報をリストで、昇順に格納する。 */
		final HoroscopeEntity entity = this.horoscopeService.getHoroscopeData();

		model.addAttribute("HoroscopeEntity", entity);

		return Mapping.RESOURCE_HOROSCOPE;
	}
}
