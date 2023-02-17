package jp.ac.hcs.mbraw.controller.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.ac.hcs.config.Mapping;
import jp.ac.hcs.mbraw.controller.AttributeEntity;
import lombok.extern.slf4j.Slf4j;

/** チャート情報を表示するコントローラー */
@Slf4j
@Controller
public class ChartController {

	@Autowired
	private ChartService chartService;

	@GetMapping(Mapping.MAPPING_CHART)
	public String getMainData(Model model) {
		ChartController.log.debug("GET {}", Mapping.MAPPING_CHART);

		final AttributeEntity<ChartData> entity = this.chartService.getChartData();

		model.addAttribute("chartEntity",entity.getResponseEntity().get());

		model.asMap().forEach((k, v) -> ChartController.log.debug("key={}, value={}", k, v));

		return Mapping.RESOURCE_CHART;
	}
}
