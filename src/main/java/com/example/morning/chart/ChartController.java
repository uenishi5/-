package com.example.morning.chart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChartController {
	@RequestMapping("/Chart")
	public String getMainData(Model model){
		ChartEntity entity = new ChartEntity();
		ChartService service = new ChartService();
		entity = service.getChartData();
		model.addAttribute("ChartEntity",entity);
		return "chart";
	}
}
