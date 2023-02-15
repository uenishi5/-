package jp.ac.hcs.mbraw.controller.traffic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.hcs.config.Mapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TrafficController {

	@Autowired
	private TrafficService trafficService;

	/** 交通画面に遷移 */
	@GetMapping(Mapping.MAPPING_TRAFFIC)
	public String mainTrafficData(Model model) {
		log.debug("GET {}", Mapping.MAPPING_TRAFFIC);

		final TrafficEntity entity = this.trafficService.getTrafficFlg();
		final TrafficData data = entity.getTrafficflgList().get(0);
		model.addAttribute("trafficflgList", data.getTrafficFlgList());
		System.out.println(data.getTrafficFlgList());
		// //結果を取得
		// TrafficEntity entity = trafficService.getMainTrafficData();

		model.asMap().forEach((k, v) -> log.debug("key={}, value={}", k, v));

		return Mapping.RESOURCE_TRAFFIC;
	}

	/** 交通情報を取得 */
	@GetMapping(Mapping.MAPPING_TRAFFIC_BUS)
	public String getTrafficData(Model model, @RequestParam("bus") int no) {
		log.debug("GET {}", Mapping.MAPPING_TRAFFIC_BUS);

		final TrafficEntity entity = this.trafficService.getBusdata(no);
		final TrafficData data = entity.getTrafficflgList().get(0);

		model.addAttribute("flg", data.isAlertflg());
		model.addAttribute("TrafficEntity", entity);

		model.asMap().forEach((k, v) -> log.debug("key={}, value={}", k, v));

		return Mapping.RESOURCE_TRAFFIC_RESULT;
	}
}
