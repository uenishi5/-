package jp.ac.hcs.morning.weather_alert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import jp.ac.hcs.morning.weather_alert.Weather_alertData.AlertColor;
import jp.ac.hcs.morning.weather_alert.Weather_alertData.LowerAlertData;
import jp.ac.hcs.morning.weather_alert.Weather_alertData.UpperAlertData;

/** 警報注意報を取得するクラス */
@Service
public class Weather_alertService {

	private static String URL = "https://typhoon.yahoo.co.jp/weather/jp/warn/15/15204/";

	/** メイン画面用警報・注意報取得 */
	public Weather_alertEntity getMainWeather_alertData() {
		Document document;
		try {
			document = Jsoup.connect(URL).get();
		}
		catch (IOException e1) {
			e1.printStackTrace();
			return Weather_alertEntity.error();
		}

		final Weather_alertEntity entity = new Weather_alertEntity();

		Elements getalert = document.select(".warnDetail_head");
		String[] alert = getalert.text().split(" ");
		if (alert[1].equals("発表なし")) {
			Weather_alertData data = new Weather_alertData();
			data.setName(alert[1]);
			entity.getWeather_alertnameList().add(data);
			data.setAlert_color(AlertColor.WHITE);
		}
		else {
			int alertcount = alert.length;
			for (int idx = 1; idx < alertcount; idx++) {
				Weather_alertData data = new Weather_alertData();
				if ((alert[idx].matches(".*注意報"))) {
					data.setName(alert[idx].replace("注意報", ""));

					data.setAlert_color(AlertColor.YELLOW);
				}
				else if ((alert[idx].matches(".*警報"))) {
					data.setName(alert[idx].replace("警報", ""));

					data.setAlert_color(AlertColor.RED);
				}
				else if ((alert[idx].matches(".*特別警報"))) {
					data.setName(alert[idx].replace("特別警報", ""));

					data.setAlert_color(AlertColor.BLACK);
				}
				entity.getWeather_alertnameList().add(data);
			}
		}
		return entity;
	}

	/** 警報注意報を取得するメソッド */
	public Weather_alertEntity getWeather_alertData() {
		Document document;
		try {
			document = Jsoup.connect(URL).get();
		}
		catch (IOException e) {
			e.printStackTrace();
			return Weather_alertEntity.error();
		}

		final Weather_alertEntity entity = new Weather_alertEntity();
		Elements getalert = document.select(".warnDetail_head");
		Elements getalert2 = document.select(".warnDetail_timeTable");
		Elements getdate = document.select(".warnDetail_timeTable_row-time");
		String[] alert = getalert.text().split(" ");
		String[] datelabel = getdate.text().split(" ");
		boolean weatherflg = false; // 2段目の表を表示する警報が存在するかのフラグ
		boolean alertfirst = false; // 1段目の表が存在するかのフラグ
		Weather_alertData date = Weather_alertData.empty();
		String firstdate = null;
		int alertnamecount = 14; // 警報名取得用(風雪等)の開始添え字
		int alertlabel = 16; // 警報の内容(注意報、警報)取得用の開始添え字
		String name = null;
		boolean errorflg = false; // 1段目10番目の要素が適切かのフラグ

		Elements day = document.select(".warnDetail_timeTable_row-day");
		String[] days = day.text().split(" ");
		// 警報があるかの確認
		if (alert[1].equals("発表なし")) { // 警報がない場合、発表無しの要素をを入れてentityを返す
			Weather_alertData data = new Weather_alertData();
			data.setName(alert[1]);
			data.setAlert_color(AlertColor.WHITE);
			entity.getWeather_alertnameList().add(data);
		}
		else { // 警報が存在する場合、中に入る
			String[] alertdata = getalert2.text().split(" ");
			int alertcount = alert.length;
			int alertlength = alertdata.length;
			for (int idx = 1; idx < alertcount; idx++) {
				Weather_alertData data = new Weather_alertData();
				// 2段目を表示する必要があるかのチェック
				if (alert[idx].equals("融雪注意報") || alert[idx].equals("乾燥注意報") || alert[idx].equals("なだれ注意報")
						|| alert[idx].equals("低温注意報") || alert[idx].equals("霜注意報") || alert[idx].equals("着氷注意報")) {
					weatherflg = true;
					if (idx == 1) {
						alertfirst = true; // 1段目を表示する必要が無いので、フラグをtrueにする
					}
				}
				// 警報名を取得して、dataにセットする
				if ((alert[idx].matches(".*注意報"))) { // 注意報の場合、文字列から注意報を削除して、黄色をセットする
					data.setName(alert[idx].replace("注意報", ""));
					data.setAlert_color(AlertColor.YELLOW);
				}
				else if ((alert[idx].matches(".*警報"))) { // 警報の場合、文字列から警報を削除して、赤色をセットする
					data.setName(alert[idx].replace("警報", ""));
					data.setAlert_color(AlertColor.RED);
				}
				else if ((alert[idx].matches(".*特別警報"))) { // 特別警報の場合、文字列から特別警報を削除して、黒色をセットする
					data.setName(alert[idx].replace("特別警報", ""));
					data.setAlert_color(AlertColor.BLACK);
				}
				entity.getWeather_alertnameList().add(data);
			}
			// 2段目の表のみ存在する場合、添え字を調整して日付をdataにセットする
			if (weatherflg && alertfirst) {
				for (int idx = 0, arrayIdx = 1; idx < 4; idx++, arrayIdx++) {
					date.getLowerAlertList().get(idx).setAlertData(days[arrayIdx]);
				}
				// date.setAlertdaydate1(days[1]);
				// date.setAlertdaydate2(days[2]);
				// date.setAlertdaydate3(days[3]);
				// date.setAlertdaydate4(days[4]);

				alertnamecount = 5;
				alertlabel = 7;
				// 2段目の表が存在する場合、添え字を調整して日付をdataにセットする
			}
			else if (weatherflg) {
				for (int idx = 0, arrayIdx = 4; idx < 4; idx++, arrayIdx++) {
					date.getLowerAlertList().get(idx).setAlertData(days[arrayIdx]);
				}
				// date.setAlertdaydate1(days[4]);
				// date.setAlertdaydate2(days[5]);
				// date.setAlertdaydate3(days[6]);
				// date.setAlertdaydate4(days[7]);
			}
			// 1段目の表が存在する場合、時刻をdataにセットする
			if (alertfirst == false) {
				firstdate = datelabel[1] + "時から"; // 一番始めの時刻情報と分かるように文字列の作成
				for (int idx = 0, arrayIdx = 1; idx < 10; idx++, arrayIdx++) {
					date.getUpperAlertList().get(alertlength).setTime(datelabel[arrayIdx]);
				}
				// date.setDate1(datelabel[1]);
				// date.setDate2(datelabel[2]);
				// date.setDate3(datelabel[3]);
				// date.setDate4(datelabel[4]);
				// date.setDate5(datelabel[5]);
				// date.setDate6(datelabel[6]);
				// date.setDate7(datelabel[7]);
				// date.setDate8(datelabel[8]);
				// date.setDate9(datelabel[9]);
				// date.setDate10(datelabel[10]);
			}
			entity.getWeather_dateList().add(date);
			// １段目の表の要素を取得する（存在する場合)
			while (!alertfirst) { // 1段目の表の要素が存在しない場合、処理を飛ばす
				// while(true)
				// if (alertfirst) {
				// break;
				// }

				Weather_alertData data = new Weather_alertData();
				// 警報名の取得（表の一番左）
				name = alertdata[alertnamecount];
				alertnamecount++; // 添え字を進める
				// 追加の情報がある場合、取得し続ける（"○○ 海上" 等)
				while (!(alertdata[alertnamecount].equals(firstdate))) {
					name = name + " " + alertdata[alertnamecount];
					alertnamecount++;
					alertlabel++;
				}
				alertnamecount = alertnamecount - 1; // 進めすぎた添え字を調整する
				data.setName(name); // 警報名をdataにセットする
				final List<String> alertdatalist = new ArrayList<>(); // 表の要素を格納するリスト
				final List<AlertColor> alertclasslist = new ArrayList<>(); // 表の要素に対応した色を格納するリスト
				// 表の要素を左から10個分取得
				for (int idx = 0; idx < 10; idx++) {
					// 最後の要素の不具合をチェック
					if (idx == 9) {
						// 不具合があった場合、添え字を調整してエラーフラグをtrueにする
						if (alertdata[alertlabel].equals("発表なし")) {

							addAlertData(AlertColor.WHITE, alertdatalist, alertclasslist, errorflg, alertnamecount, alertlabel, alertdata);
							// 要素をdatalistにセットする
							// alertdatalist.add(alertdata[alertlabel]);
							// // 要素に対応した色をclasslistにセットする
							// alertclasslist.add(AlertColor.WHITE);
							// // 不具合が存在したので、エラーフラグをtrueにする
							// errorflg = true;
							// // 添え字の調整
							// alertlabel = alertlabel + 1;
							// alertnamecount = alertnamecount - 1;
							// 以下のelse if文は上記と同様の動作
						}
						else if (alertdata[alertlabel].equals("注意報級")) {
							addAlertData(AlertColor.YELLOW, alertdatalist, alertclasslist, errorflg, alertnamecount, alertlabel, alertdata);
							// alertdatalist.add(alertdata[alertlabel]);
							// alertclasslist.add(AlertColor.YELLOW);
							// errorflg = true;
							// alertlabel = alertlabel + 1;
							// alertnamecount = alertnamecount - 1;
						}
						else if (alertdata[alertlabel].equals("警報級")) {
							addAlertData(AlertColor.RED, alertdatalist, alertclasslist, errorflg, alertnamecount, alertlabel, alertdata);
							// alertdatalist.add(alertdata[alertlabel]);
							// alertclasslist.add(AlertColor.RED);
							// errorflg = true;
							// alertlabel = alertlabel + 1;
							// alertnamecount = alertnamecount - 1;
						}
						else if (alertdata[alertlabel].equals("特別警報級")) {
							addAlertData(AlertColor.BLACK, alertdatalist, alertclasslist, errorflg, alertnamecount, alertlabel, alertdata);
							// alertdatalist.add(alertdata[alertlabel]);
							// alertclasslist.add(AlertColor.BLACK);
							// errorflg = true;
							// alertlabel = alertlabel + 1;
							// alertnamecount = alertnamecount - 1;
							// ここまで
						}
						else {
							alertlabel = alertlabel + 1; // 不具合が存在しなかった場合、添え字を調整する
						}
						// 不具合が存在しなかった場合、通常通りに要素を取得する
						if (!(errorflg)) {
							// 要素をdatalistにセットする
							alertdatalist.add(alertdata[alertlabel]);
							// 要素に対応した色をclasslistにセットする
							alertclasslist.add(AlertColor.parseMsg(alertdata[alertlabel]));
							// if (alertdata[alertlabel].equals("発表なし")) {
							// alertclasslist.add("white"); // 要素に対応した色をclasslistにセットする
							// } else if (alertdata[alertlabel].equals("注意報級")) {
							// alertclasslist.add("yellow");
							// } else if (alertdata[alertlabel].equals("警報級")) {
							// alertclasslist.add("red");
							// } else if (alertdata[alertlabel].equals("特別警報級")) {
							// alertclasslist.add("black");
							// }
						}
						// １～９の要素の場合
					}
					else {
						// 要素をdatalistにセットする
						alertdatalist.add(alertdata[alertlabel]);

						// 要素に対応した色をdayalertclasslistにセットする
						alertclasslist.add(AlertColor.parseMsg(alertdata[alertlabel]));
						// if (alertdata[alertlabel].equals("発表なし")) {
						// alertclasslist.add("white"); // 要素に対応した色をclasslistにセットする
						// } else if (alertdata[alertlabel].equals("注意報級")) {
						// alertclasslist.add("yellow");
						// } else if (alertdata[alertlabel].equals("警報級")) {
						// alertclasslist.add("red");
						// } else if (alertdata[alertlabel].equals("特別警報級")) {
						// alertclasslist.add("black");
						// }

						// 次が10個目の要素を取得する場合、添え字調整を行う

						final int adjustNum = idx == 8 ? 1 : 2;

						alertlabel += adjustNum;

						// if (idx == 8) {
						// alertlabel = alertlabel + 1;
						// } else {
						// alertlabel = alertlabel + 2;
						// }
					}
				}
				// dataに取得した要素の格納を行う

				for (int i = 0; i < 10; i++) {
					final UpperAlertData upperAlertData = date.getUpperAlertList().get(i);
					upperAlertData.setAlertData(alertdatalist.get(i));
					upperAlertData.setAlertDataClass(alertclasslist.get(i));
				}

				// data.setAlertdata1(alertdatalist.get(0));
				// data.setAlertdata1class(alertclasslist.get(0));
				// data.setAlertdata2(alertdatalist.get(1));
				// data.setAlertdata2class(alertclasslist.get(1));
				// data.setAlertdata3(alertdatalist.get(2));
				// data.setAlertdata3class(alertclasslist.get(2));
				// data.setAlertdata4(alertdatalist.get(3));
				// data.setAlertdata4class(alertclasslist.get(3));
				// data.setAlertdata5(alertdatalist.get(4));
				// data.setAlertdata5class(alertclasslist.get(4));
				// data.setAlertdata6(alertdatalist.get(5));
				// data.setAlertdata6class(alertclasslist.get(5));
				// data.setAlertdata7(alertdatalist.get(6));
				// data.setAlertdata7class(alertclasslist.get(6));
				// data.setAlertdata8(alertdatalist.get(7));
				// data.setAlertdata8class(alertclasslist.get(7));
				// data.setAlertdata9(alertdatalist.get(8));
				// data.setAlertdata9class(alertclasslist.get(8));
				// data.setAlertdata10(alertdatalist.get(9));
				// data.setAlertdata10class(alertclasslist.get(9));

				entity.getWeather_alertList().add(data);

				// まだ取得する要素が存在しているかのチェックを行う。存在しない場合はループを抜ける
				if (alertnamecount + 21 < alertlength - 1 && !(alertdata[alertnamecount + 21].equals("日付"))) {
					// 添え字の調整
					alertnamecount = alertnamecount + 21;
					alertlabel = alertlabel + 3;
				}
				else {
					break;
				}
			}
			// 2段目の表の要素が存在し、1段目の表が存在した場合、添え字の調整を行う
			if (weatherflg && !(alertfirst)) {
				alertnamecount = alertnamecount + 26;
				alertlabel = alertlabel + 8;
			}
			// 2段目の表の要素を取得する（存在する場合)
			while (weatherflg) {
				Weather_alertData data = new Weather_alertData();
				name = alertdata[alertnamecount];
				alertnamecount++;
				// 追加の情報がある場合、取得し続ける（"○○ 海上" 等)
				while (!(alertdata[alertnamecount].matches(".*日"))) {
					name = name + " " + alertdata[alertnamecount];
					alertnamecount++;
					alertlabel++;
				}
				alertnamecount = alertnamecount - 1; // 添え字の調整
				data.setName(name);
				List<String> dayalertlist = new ArrayList<>();
				List<AlertColor> dayalertclasslist = new ArrayList<>();
				// 4日分の要素を取得する
				for (int idx = 0; idx < 4; idx++) {

					// 要素をdayalertlistに格納する
					dayalertlist.add(alertdata[alertlabel]);

					// 要素に対応した色をdayalertclasslistにセットする
					dayalertclasslist.add(AlertColor.parseMsg(alertdata[alertlabel]));

					// if (alertdata[alertlabel].equals("発表なし")) {
					// dayalertclasslist.add("white"); // 要素に対応した色をdayalertclasslistにセットする
					// } else if (alertdata[alertlabel].equals("注意報級")) {
					// dayalertclasslist.add("yellow");
					// } else if (alertdata[alertlabel].equals("警報級")) {
					// dayalertclasslist.add("red");
					// } else if (alertdata[alertlabel].equals("特別警報級")) {
					// dayalertclasslist.add("black");
					// }
					// 最後の要素を取得する場合、添え字の調整を行う
					if (!(idx == 3)) {
						alertlabel = alertlabel + 2;
					}
				}
				// dataに要素の格納を行う

				for (int i = 0; i < 4; i++) {
					final LowerAlertData lowerAlertData = date.getLowerAlertList().get(i);
					lowerAlertData.setAlertData(dayalertlist.get(0));
					lowerAlertData.setAlertDataClass(dayalertclasslist.get(i));
				}

				// data.setAlert1(dayalertlist.get(0));
				// data.setDayalertdata1class(dayalertclasslist.get(0));
				// data.setAlert2(dayalertlist.get(1));
				// data.setDayalertdata2class(dayalertclasslist.get(1));
				// data.setAlert3(dayalertlist.get(2));
				// data.setDayalertdata3class(dayalertclasslist.get(2));
				// data.setAlert4(dayalertlist.get(3));
				// data.setDayalertdata4class(dayalertclasslist.get(3));

				entity.getWeather_alert2List().add(data);
				// まだ取得する要素が存在しているかのチェック。存在しない場合はループを抜ける
				if (alertnamecount + 9 > alertlength - 1) {
					// 添え字の調整
					alertnamecount = alertnamecount + 9;
					alertlabel = alertlabel + 3;
					break;
				}
				else {}
			}

		}
		// 例外処理 entity.setError(true);

		return entity;

	}

	public static void addAlertData(AlertColor alertColor, List<String> alertdatalist, List<AlertColor> alertclasslist, boolean errorflg, int alertnamecount, int alertlabel, String[] alertdata) {
		alertdatalist.add(alertdata[alertlabel]);
		alertclasslist.add(alertColor);
		errorflg = true;
		alertlabel = alertlabel + 1;
		alertnamecount = alertnamecount - 1;
	}
}