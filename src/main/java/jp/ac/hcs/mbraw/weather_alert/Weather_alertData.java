package jp.ac.hcs.mbraw.weather_alert;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.api.client.util.Lists;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class Weather_alertData {

	private static int UPPER_DATA_SIZE = 10;
	private static int LOWER_DATA_SIZE = 4;

	/** メイン画面に表示する警報内容 */
	private String alertname;

	/** 警報、注意報の数 */
	private int alertcount;

	/** 警報、注意報の内容 */
	private String name;

	/** 上の警報のアイテムが必ず１０個入る */
	private final List<UpperAlertData> upperAlertList = Lists.newArrayListWithCapacity(UPPER_DATA_SIZE);

	/** 下の警報のアイテムが必ず４個入る */
	private final List<LowerAlertData> lowerAlertList = Lists.newArrayListWithCapacity(LOWER_DATA_SIZE);

	/** 注意報,警報、緊急警報の色 */
	private AlertColor alert_color;

	/** エラーキャッチフラグ */
	private boolean catchflg;
	
	/** 1つ目のcolspan */
	private int colspan1;
	
	/** 2つ目のcolspan */
	private int colspan2;

	/** 空のWeather_alertDataオブジェクトを返す */
	public static Weather_alertData empty() {
		final Weather_alertData data = new Weather_alertData();

		for (int i = 0; i < UPPER_DATA_SIZE; i++) {
			data.upperAlertList.add(new UpperAlertData());
		}

		for (int i = 0; i < LOWER_DATA_SIZE; i++) {
			data.lowerAlertList.add(new LowerAlertData());
		}

		return data;
	}

	public static Weather_alertData noneAlert() {
		final Weather_alertData weatherAlertData = new Weather_alertData();

		weatherAlertData.setName("発表なし");
		weatherAlertData.setAlert_color(AlertColor.WHITE);

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

	@Getter
	@RequiredArgsConstructor
	public static enum AlertColor {
		WHITE("発表なし"),
		YELLOW("注意報級", ".*注意報"),
		RED("警報級", ".*警報"),
		BLACK("特別警報級", ".*特別警報");

		private final String msg;
		private final String reg;

		private AlertColor(String msg) {
			this(msg, null);
		}

		public static AlertColor parseMsg(String text) {
			for (AlertColor color : values()) {
				if (text.equals(color.getMsg())) {
					return color;
				}
			}
			return WHITE;
		}

		public static AlertColor parseReg(String text) {
			for (AlertColor color : values()) {
				if (text.matches(color.getReg())) {
					return color;
				}
			}
			return WHITE;
		}
	}
	public static int Colspan (String url){
		Document document;
		int colspan = 0;
		try {
			document = Jsoup.connect(url).get();
			Elements pagedata = document.select(".warnDetail_timeTable_row-day");
			Elements thdata = pagedata.select("th");
			for(Element th : thdata) {
			String strcolspan = th.attr("colspan");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return colspan;
	}
}
