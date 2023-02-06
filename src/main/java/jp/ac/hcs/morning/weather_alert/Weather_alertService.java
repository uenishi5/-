package jp.ac.hcs.morning.weather_alert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/** 警報注意報を取得するクラス */
@Service
public class Weather_alertService {

	/** メイン画面用警報・注意報取得 */
	public Weather_alertEntity getMainWeather_alertData() {
		Document document;
		Weather_alertEntity entity = new Weather_alertEntity();

		try {
			document = Jsoup.connect("https://typhoon.yahoo.co.jp/weather/jp/warn/1b/1100/").get();

			Elements getalert = document.select(".warnDetail_head");
			String[] alert = getalert.text().split(" ");
			if (alert[1].equals("発表なし")) {
				Weather_alertData data = new Weather_alertData();
				data.setName(alert[1]);
				entity.getWeather_alertnameList().add(data);
				data.setAlert_color("white");
			} else {
				int alertcount = alert.length;
				for (int idx = 1; idx < alertcount; idx++) {
					Weather_alertData data = new Weather_alertData();
					if ((alert[idx].matches(".*注意報"))) {
						data.setName(alert[idx].replace("注意報", ""));

						data.setAlert_color("yellow");
					} else if ((alert[idx].matches(".*警報"))) {
						data.setName(alert[idx].replace("警報", ""));

						data.setAlert_color("red");
					} else if ((alert[idx].matches(".*特別警報"))) {
						data.setName(alert[idx].replace("特別警報", ""));

						data.setAlert_color("black");
					}
					entity.getWeather_alertnameList().add(data);
				}
			}
		} catch (IOException e) {
			Weather_alertData data = new Weather_alertData();
			data.setCatchflg(true);
			data.setName("エラー");
			data.setAlert_color("red");
			entity.getWeather_alertnameList();
		}
		return entity;
	}

	/** 警報注意報を取得するメソッド */
	public Weather_alertEntity getWeather_alertData() {
		Document document;
		Weather_alertEntity entity = new Weather_alertEntity();
		entity.setError(false);
		try {
			// URL指定
			document = Jsoup.connect("https://typhoon.yahoo.co.jp/weather/jp/warn/1b/1100/").get();

			Elements getalert = document.select(".warnDetail_head");
			Elements getalert2 = document.select(".warnDetail_timeTable");
			Elements getdate = document.select(".warnDetail_timeTable_row-time");
			String[] alert = getalert.text().split(" ");
			String[] datelabel = getdate.text().split(" ");
			boolean weatherflg = false; // 2段目の表を表示する警報が存在するかのフラグ
			boolean alertfirst = false; // 1段目の表が存在するかのフラグ
			Weather_alertData date = new Weather_alertData();
			String firstdate = null;
			int alertnamecount = 14; //警報名取得用(風雪等)の開始添え字
			int alertlabel = 16; // 警報の内容(注意報、警報)取得用の開始添え字
			String name = null;
			boolean errorflg = false; //1段目10番目の要素が適切かのフラグ

			Elements day = document.select(".warnDetail_timeTable_row-day");
			String[] days = day.text().split(" ");
			// 警報があるかの確認
			if (alert[1].equals("発表なし")) { //警報がない場合、発表無しの要素をを入れてentityを返す
				Weather_alertData data = new Weather_alertData();
				data.setName(alert[1]);
				data.setAlert_color("white");
				entity.getWeather_alertnameList().add(data);
			} else { //警報が存在する場合、中に入る
				String[] alertdata = getalert2.text().split(" ");
				int alertcount = alert.length;
				int alertlength = alertdata.length;
				for (int idx = 1; idx < alertcount; idx++) {
					Weather_alertData data = new Weather_alertData();
					//2段目を表示する必要があるかのチェック
					if (alert[idx].equals("融雪注意報") || alert[idx].equals("乾燥注意報") || alert[idx].equals("なだれ注意報")
							|| alert[idx].equals("低温注意報") || alert[idx].equals("霜注意報") || alert[idx].equals("着氷注意報")) {
						weatherflg = true;
						if (idx == 1) {
							alertfirst = true; //1段目を表示する必要が無いので、フラグをtrueにする
						}
					}
					//警報名を取得して、dataにセットする
					if ((alert[idx].matches(".*注意報"))) { // 注意報の場合、文字列から注意報を削除して、黄色をセットする
						data.setName(alert[idx].replace("注意報", ""));
						data.setAlert_color("yellow");
					} else if ((alert[idx].matches(".*警報"))) { // 警報の場合、文字列から警報を削除して、赤色をセットする
						data.setName(alert[idx].replace("警報", ""));
						data.setAlert_color("red");
					} else if ((alert[idx].matches(".*特別警報"))) { // 特別警報の場合、文字列から特別警報を削除して、黒色をセットする
						data.setName(alert[idx].replace("特別警報", ""));
						data.setAlert_color("black");
					}
					entity.getWeather_alertnameList().add(data);
				}
				// 2段目の表のみ存在する場合、添え字を調整して日付をdataにセットする
				if (weatherflg && alertfirst) {
					date.setAlertdaydate1(days[1]);
					date.setAlertdaydate2(days[2]);
					date.setAlertdaydate3(days[3]);
					date.setAlertdaydate4(days[4]);
					alertnamecount = 5;
					alertlabel = 7;
					// 2段目の表が存在する場合、添え字を調整して日付をdataにセットする
				} else if (weatherflg) {
					date.setAlertdaydate1(days[4]);
					date.setAlertdaydate2(days[5]);
					date.setAlertdaydate3(days[6]);
					date.setAlertdaydate4(days[7]);
				}
				// 1段目の表が存在する場合、時刻をdataにセットする
				if (alertfirst == false) {
					firstdate = datelabel[1] + "時から"; // 一番始めの時刻情報と分かるように文字列の作成
					date.setDate1(datelabel[1]);
					date.setDate2(datelabel[2]);
					date.setDate3(datelabel[3]);
					date.setDate4(datelabel[4]);
					date.setDate5(datelabel[5]);
					date.setDate6(datelabel[6]);
					date.setDate7(datelabel[7]);
					date.setDate8(datelabel[8]);
					date.setDate9(datelabel[9]);
					date.setDate10(datelabel[10]);
				}
				entity.getWeather_dateList().add(date);
				//１段目の表の要素を取得する（存在する場合)
				while (true) { // 1段目の表の要素が存在しない場合、処理を飛ばす
					if (alertfirst) {
						break;
					}

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
					List<String> alertdatalist = new ArrayList<String>(); // 表の要素を格納するリスト
					List<String> alertclasslist = new ArrayList<String>(); // 表の要素に対応した色を格納するリスト
					// 表の要素を左から10個分取得
					for (int idx = 0; idx < 10; idx++) {
						// 最後の要素の不具合をチェック
						if (idx == 9) {
							// 不具合があった場合、添え字を調整してエラーフラグをtrueにする
							if (alertdata[alertlabel].equals("発表なし")) {
								alertdatalist.add(alertdata[alertlabel]); // 要素をdatalistにセットする
								alertclasslist.add("white"); // 要素に対応した色をclasslistにセットする
								errorflg = true; // 不具合が存在したので、エラーフラグをtrueにする
								// 添え字の調整
								alertlabel = alertlabel + 1;
								alertnamecount = alertnamecount - 1;
								//以下のelse if文は上記と同様の動作
							} else if (alertdata[alertlabel].equals("注意報級")) {
								alertdatalist.add(alertdata[alertlabel]);
								alertclasslist.add("yellow");
								errorflg = true;
								alertlabel = alertlabel + 1;
								alertnamecount = alertnamecount - 1;
							} else if (alertdata[alertlabel].equals("警報級")) {
								alertdatalist.add(alertdata[alertlabel]);
								alertclasslist.add("red");
								errorflg = true;
								alertlabel = alertlabel + 1;
								alertnamecount = alertnamecount - 1;
							} else if (alertdata[alertlabel].equals("特別警報級")) {
								alertdatalist.add(alertdata[alertlabel]);
								alertclasslist.add("black");
								errorflg = true;
								alertlabel = alertlabel + 1;
								alertnamecount = alertnamecount - 1;
								// ここまで
							} else {
								alertlabel = alertlabel + 1; // 不具合が存在しなかった場合、添え字を調整する
							}
							// 不具合が存在しなかった場合、通常通りに要素を取得する
							if (!(errorflg)) {
								alertdatalist.add(alertdata[alertlabel]); // 要素をdatalistにセットする
								if (alertdata[alertlabel].equals("発表なし")) {
									alertclasslist.add("white"); // 要素に対応した色をclasslistにセットする
								} else if (alertdata[alertlabel].equals("注意報級")) {
									alertclasslist.add("yellow");
								} else if (alertdata[alertlabel].equals("警報級")) {
									alertclasslist.add("red");
								} else if (alertdata[alertlabel].equals("特別警報級")) {
									alertclasslist.add("black");
								}
							}
							// １～９の要素の場合
						} else {
							alertdatalist.add(alertdata[alertlabel]); // 要素をdatalistにセットする
							if (alertdata[alertlabel].equals("発表なし")) {
								alertclasslist.add("white"); // 要素に対応した色をclasslistにセットする
							} else if (alertdata[alertlabel].equals("注意報級")) {
								alertclasslist.add("yellow");
							} else if (alertdata[alertlabel].equals("警報級")) {
								alertclasslist.add("red");
							} else if (alertdata[alertlabel].equals("特別警報級")) {
								alertclasslist.add("black");
							}
							// 次が10個目の要素を取得する場合、添え字調整を行う
							if (idx == 8) {
								alertlabel = alertlabel + 1;
							} else {
								alertlabel = alertlabel + 2;
							}
						}
					}
					// dataに取得した要素の格納を行う
					data.setAlertdata1(alertdatalist.get(0));
					data.setAlertdata1class(alertclasslist.get(0));
					data.setAlertdata2(alertdatalist.get(1));
					data.setAlertdata2class(alertclasslist.get(1));
					data.setAlertdata3(alertdatalist.get(2));
					data.setAlertdata3class(alertclasslist.get(2));
					data.setAlertdata4(alertdatalist.get(3));
					data.setAlertdata4class(alertclasslist.get(3));
					data.setAlertdata5(alertdatalist.get(4));
					data.setAlertdata5class(alertclasslist.get(4));
					data.setAlertdata6(alertdatalist.get(5));
					data.setAlertdata6class(alertclasslist.get(5));
					data.setAlertdata7(alertdatalist.get(6));
					data.setAlertdata7class(alertclasslist.get(6));
					data.setAlertdata8(alertdatalist.get(7));
					data.setAlertdata8class(alertclasslist.get(7));
					data.setAlertdata9(alertdatalist.get(8));
					data.setAlertdata9class(alertclasslist.get(8));
					data.setAlertdata10(alertdatalist.get(9));
					data.setAlertdata10class(alertclasslist.get(9));
					entity.getWeather_alertList().add(data);
					// まだ取得する要素が存在しているかのチェックを行う。存在しない場合はループを抜ける
					if (alertnamecount + 21 < alertlength - 1 && !(alertdata[alertnamecount + 21].equals("日付"))) {
						// 添え字の調整
						alertnamecount = alertnamecount + 21;
						alertlabel = alertlabel + 3;
					} else {
						break;
					}
				}
				// 2段目の表の要素が存在し、1段目の表が存在した場合、添え字の調整を行う
				if (weatherflg && !(alertfirst)) {
					alertnamecount = alertnamecount + 26;
					alertlabel = alertlabel + 8;
				}
				//2段目の表の要素を取得する（存在する場合)
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
					List<String> dayalertlist = new ArrayList<String>();
					List<String> dayalertclasslist = new ArrayList<String>();
					// 4日分の要素を取得する
					for (int idx = 0; idx < 4; idx++) {
						dayalertlist.add(alertdata[alertlabel]); // 要素をdayalertlistに格納する
						if (alertdata[alertlabel].equals("発表なし")) {
							dayalertclasslist.add("white"); // 要素に対応した色をdayalertclasslistにセットする
						} else if (alertdata[alertlabel].equals("注意報級")) {
							dayalertclasslist.add("yellow");
						} else if (alertdata[alertlabel].equals("警報級")) {
							dayalertclasslist.add("red");
						} else if (alertdata[alertlabel].equals("特別警報級")) {
							dayalertclasslist.add("black");
						}
						// 最後の要素を取得する場合、添え字の調整を行う
						if (!(idx == 3)) {
							alertlabel = alertlabel + 2;
						}
					}
					// dataに要素の格納を行う
					data.setAlert1(dayalertlist.get(0));
					data.setDayalertdata1class(dayalertclasslist.get(0));
					data.setAlert2(dayalertlist.get(1));
					data.setDayalertdata2class(dayalertclasslist.get(1));
					data.setAlert3(dayalertlist.get(2));
					data.setDayalertdata3class(dayalertclasslist.get(2));
					data.setAlert4(dayalertlist.get(3));
					data.setDayalertdata4class(dayalertclasslist.get(3));
					entity.getWeather_alert2List().add(data);
					// まだ取得する要素が存在しているかのチェック。存在しない場合はループを抜ける
					if (alertnamecount + 9 > alertlength - 1) {
						// 添え字の調整
						alertnamecount = alertnamecount + 9;
						alertlabel = alertlabel + 3;
						break;
					} else {
					}
				}

			}
			// 例外処理
		} catch (IOException e) {

			entity.setError(true);
			return entity;

		}
		return entity;
	}
}