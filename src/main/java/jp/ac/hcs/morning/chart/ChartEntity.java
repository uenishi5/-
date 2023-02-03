package jp.ac.hcs.morning.chart;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ChartEntity {
	private final List<ChartData> chartList = new ArrayList<>();

	public static ChartEntity error() {
		final ChartEntity chartEntity = new ChartEntity();
		final ChartData chartData = new ChartData();

		chartData.setMaxrate("取得できませんでした");
		chartData.setMinrate("取得できませんでした");
		chartData.setMarket_capitalization("取得出来ませんでした");
		chartEntity.getChartList().add(chartData);

		return chartEntity;
	}
}
