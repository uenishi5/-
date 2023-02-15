package jp.ac.hcs.mbraw.controller.horoscope;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.ac.hcs.config.EntityHolder;
import jp.ac.hcs.config.Mapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HoroscopeController {

	@Autowired
	private HoroscopeService horoscopeService;

	/** 占い画面に遷移するコントローラー */
	@GetMapping(Mapping.MAPPING_HOROSCOPE)
	public String getHoroscopeData(Model model) {
		log.debug("GET {}", Mapping.MAPPING_HOROSCOPE);

		/** 全ての星座情報をリストで、昇順に格納する。 */
		final HoroscopeEntity entity = new HoroscopeEntity();

		if (Objects.isNull(EntityHolder.HOROSCOPE_TODAY)) {
			EntityHolder.HOROSCOPE_TODAY = this.horoscopeService.getHoroscopeData().getHoroscopeList();
		}

		entity.getHoroscopeList().addAll(EntityHolder.HOROSCOPE_TODAY);
		model.addAttribute("HoroscopeEntity", entity);

		model.asMap().forEach((k, v) -> log.debug("key={}, value={}", k, v));

		return Mapping.RESOURCE_HOROSCOPE;
	}
}
