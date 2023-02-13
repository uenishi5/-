package jp.ac.hcs.mbraw.controller.traffic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.hcs.config.Mapping;

@Controller
public class TrafficController {

	@Autowired
	private TrafficService trafficService;

	/** 交通画面に遷移 */
	@RequestMapping("/Traffic")
	public String mainTrafficData(Model model) {
		final TrafficEntity entity = this.trafficService.getTrafficFlg();
		final TrafficData data = entity.getTrafficflgList().get(0);
		model.addAttribute("trafficflgList" , data.getTrafficFlgList());
		System.out.println(data.getTrafficFlgList());
		// //結果を取得
		// TrafficEntity entity = trafficService.getMainTrafficData();

		return Mapping.RESOURCE_TRAFFIC;
	}

	/** 交通情報を取得 */
	@GetMapping("/TrafficBus")
	public String getTrafficData(Model model, @RequestParam("bus") int no) {
		final TrafficEntity entity = this.trafficService.getBusdata(no);
		final TrafficData data = entity.getTrafficflgList().get(0);

		model.addAttribute("flg", data.isAlertflg());
		model.addAttribute("TrafficEntity", entity);

		return Mapping.RESOURCE_TRAFFIC_RESULT;
	}
}
