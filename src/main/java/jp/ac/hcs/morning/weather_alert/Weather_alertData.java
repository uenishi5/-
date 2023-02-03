package jp.ac.hcs.morning.weather_alert;

import java.util.List;

import lombok.Data;

@Data
public class Weather_alertData {

	/** メイン画面に表示する警報内容 */
	private String alertname;

	/** 警報、注意報の数 */
	private int alertcount;

	/** 警報、注意報の内容 */
	private String name;

	/** 上の警報のアイテムが必ず１０個入る */
	private List<UpperAlertData> upperAlertList;

	/** 下の警報のアイテムが必ず４個入る */
	private List<LowerAlertData> lowerAlertList;

	/** 注意報,警報、緊急警報の色 */
	private String alert_color;

	/** エラーキャッチフラグ */
	private boolean catchflg;

	public static Weather_alertData noneAlert() {
		final Weather_alertData weatherAlertData = new Weather_alertData();

		weatherAlertData.setName("発表なし");
		weatherAlertData.setAlert_color("white");

		return weatherAlertData;
	}

	@Data
	public static class UpperAlertData {
		/** 時間情報 （「風雪」など） */
		private String time;
		/** 警報情報 */
		private String alertData;
		/** 埋め込みクラス(white,yellow,red,black) */
		private AlertColor alertDataClass;
	}

	@Data
	public static class LowerAlertData {
		/** 警報情報 （「なだれ」など） */
		private String alertData;
		/** 埋め込みクラス(white,yellow,red,black) */
		private AlertColor alertDataClass;
	}

	public static enum AlertColor {
		WHITE,
		YELLOW,
		RED,
		BLACK;
	}
}
