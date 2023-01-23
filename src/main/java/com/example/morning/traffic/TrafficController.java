package com.example.morning.traffic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class TrafficController {

	/** 交通画面に遷移*/
	@RequestMapping("/Traffic")
	public  String mainTrafficData(Model model) {
//		//結果を取得
//		TrafficEntity entity = trafficService.getMainTrafficData();

		return "morning/traffic";
	}
	/** 交通情報を取得*/
	@GetMapping("/TrafficBus")
	public String getTrafficData(Model model, @RequestParam("bus") int no) {
		TrafficService service = new TrafficService();
		TrafficEntity entity  = new TrafficEntity();
		entity = service.getBusdata(no);
		TrafficData a = entity.getTrafficflgList().get(0);
		boolean alertflg = a.isAlertflg();
		if(alertflg) {
			model.addAttribute("flg", true);
		}else {
			model.addAttribute("flg", false);
		}
		model.addAttribute("TrafficEntity",entity);
		
		return "morning/traffic_result";
	}
}

