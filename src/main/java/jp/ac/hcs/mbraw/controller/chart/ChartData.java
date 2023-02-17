package jp.ac.hcs.mbraw.controller.chart;

import lombok.Data;

@Data
public class ChartData {
	/** ビットコインの現在価値を格納 */
	private String bitcoin;

	/** 前回の値との差を格納 */
	private String rate;

	/** 最高値（２４時間） */
	private String maxRate;

	/** 最低値（２４時間） */
	private String minRate;

	/** 時価総額 */
	private String marketCapitalization;

	/** エラーキャッチフラグ */
	private boolean hasError = false;

	public static ChartData error() {
		final ChartData data = new ChartData();

		data.setMaxRate("取得できませんでした");
		data.setMinRate("取得できませんでした");
		data.setMarketCapitalization("取得出来ませんでした");

		return data;
	}
}
