package jp.ac.hcs.mbraw.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.ac.hcs.config.Mapping;

/** チャート情報を表示するコントローラー */
@Controller
public class ChartController {

	@Autowired
	private ChartService chartService;

	@RequestMapping("/Chart")
	public String getMainData(Model model) {
		final ChartEntity entity = this.chartService.getChartData();

		model.addAttribute("ChartEntity", entity);

		return Mapping.RESOURCE_CHART;
	}
}
