package com.example.morning.horoscope;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HoroscopeController {
	/** 占い画面に遷移するコントローラー */
	@RequestMapping("/Horoscope")
	public static String getHoroscopeData(Model model) {
		HoroscopeService service = new HoroscopeService();
		HoroscopeEntity entity = new HoroscopeEntity();
		/** 全ての星座情報をリストで、昇順に格納する。*/
		entity = service.getHoroscopeData();
		model.addAttribute("HoroscopeEntity",entity);
		return "divination";
	}
//	/**
//	 * 
//	 * @param model データの格納先
//	 * @param horotitle 星座名
//	 * @return
//	 */
//	@RequestMapping("/getHoroscopeDetail")
//	public static String getHoroscopeDetailData(Model model, String horotitle) {
//		HoroscopeEntity entity = new HoroscopeEntity();
//		HoroscopeService service = new HoroscopeService();
//		entity = service.getHoroscopeDetail(horotitle);
//		model.addAttribute("HoroscopeEntity", entity);
//		return "divination";
//	}
}
