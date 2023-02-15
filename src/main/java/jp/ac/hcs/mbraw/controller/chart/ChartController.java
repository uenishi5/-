package jp.ac.hcs.mbraw.controller.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.ac.hcs.config.Mapping;
import lombok.extern.slf4j.Slf4j;

/** チャート情報を表示するコントローラー */
@Slf4j
@Controller
public class ChartController {

	@Autowired
	private ChartService chartService;

	@GetMapping(Mapping.MAPPING_CHART)
	public String getMainData(Model model) {
		log.debug("GET {}", Mapping.MAPPING_CHART);

		final ChartEntity entity = this.chartService.getChartData();

		model.addAttribute("ChartEntity", entity);

		model.asMap().forEach((k, v) -> log.debug("key={}, value={}", k, v));

		return Mapping.RESOURCE_CHART;
	}
}
