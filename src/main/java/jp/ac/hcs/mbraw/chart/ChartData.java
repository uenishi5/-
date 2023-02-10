package jp.ac.hcs.mbraw.chart;

import lombok.Data;

@Data
public class ChartData {
	/** ビットコインの現在価値を格納 */
	private String bitcoin;
	
	/** 前回の値との差を格納 */
	private String rate;
	
	/** 最高値（２４時間）*/
	private String maxrate;
	
	/** 最低値（２４時間）*/
	private String minrate;
	
	/** 時価総額 */
	private String market_capitalization;
	
	/** エラーキャッチフラグ*/
	private boolean catchflg;
}
