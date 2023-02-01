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
			boolean weatherflg = false;
			boolean alertfirst = false;
			Weather_alertData date = new Weather_alertData();
			String firstdate = null;
			int alertnamecount = 14;
			int alertlabel = 16;
			String name = null;
			boolean errorflg = false;

			Elements day = document.select(".warnDetail_timeTable_row-day");
			String[] days = day.text().split(" ");
			// 警報があるかの確認
			if (alert[1].equals("発表なし")) {
				Weather_alertData data = new Weather_alertData();
				data.setName(alert[1]);
				data.setAlert_color("white");
				entity.getWeather_alertnameList().add(data);
			} else {
				String[] alertdata = getalert2.text().split(" ");
				int alertcount = alert.length;
				int alertlength = alertdata.length;
				for (int idx = 1; idx < alertcount; idx++) {
					Weather_alertData data = new Weather_alertData();

					if (alert[idx].equals("融雪注意報") || alert[idx].equals("乾燥注意報") || alert[idx].equals("なだれ注意報")
							|| alert[idx].equals("低温注意報") || alert[idx].equals("霜注意報") || alert[idx].equals("着氷注意報")) {
						weatherflg = true;
						if (idx == 1) {
							alertfirst = true;
						}
					}
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
				// データの取得
				if (weatherflg && alertfirst) {
					date.setAlertdaydate1(days[1]);
					date.setAlertdaydate2(days[2]);
					date.setAlertdaydate3(days[3]);
					date.setAlertdaydate4(days[4]);
					alertnamecount = 5;
					alertlabel = 7;
				} else if (weatherflg) {
					date.setAlertdaydate1(days[4]);
					date.setAlertdaydate2(days[5]);
					date.setAlertdaydate3(days[6]);
					date.setAlertdaydate4(days[7]);
				}

				if (alertfirst == false) {
					firstdate = datelabel[1] + "時から";
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
				//１段目のデータを取得する（存在する場合)
				while (true) {
					if (alertfirst) {
						break;
					}

					Weather_alertData data = new Weather_alertData();
					name = alertdata[alertnamecount];
					alertnamecount++;
					while (!(alertdata[alertnamecount].equals(firstdate))) {
						name = name + " " + alertdata[alertnamecount];
						alertnamecount++;
						alertlabel++;
					}
					alertnamecount = alertnamecount - 1;
					data.setName(name);
					List<String> alertdatalist = new ArrayList<String>();
					List<String> alertclasslist = new ArrayList<String>();
					for (int idx = 0; idx < 10; idx++) {
						if (idx == 9) {
							if (alertdata[alertlabel].equals("発表なし")) {
								alertdatalist.add(alertdata[alertlabel]);
								alertclasslist.add("white");
								errorflg = true;
								alertlabel = alertlabel + 1;
								alertnamecount = alertnamecount - 1;
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
							} else {
								alertlabel = alertlabel + 1;
							}
							if (!(errorflg)) {
								alertdatalist.add(alertdata[alertlabel]);
								if (alertdata[alertlabel].equals("発表なし")) {
									alertclasslist.add("white");
								} else if (alertdata[alertlabel].equals("注意報級")) {
									alertclasslist.add("yellow");
								} else if (alertdata[alertlabel].equals("警報級")) {
									alertclasslist.add("red");
								} else if (alertdata[alertlabel].equals("特別警報級")) {
									alertclasslist.add("black");
								}
							}
						} else {
							alertdatalist.add(alertdata[alertlabel]);
							if (alertdata[alertlabel].equals("発表なし")) {
								alertclasslist.add("white");
							} else if (alertdata[alertlabel].equals("注意報級")) {
								alertclasslist.add("yellow");
							} else if (alertdata[alertlabel].equals("警報級")) {
								alertclasslist.add("red");
							} else if (alertdata[alertlabel].equals("特別警報級")) {
								alertclasslist.add("black");
							}
							if (idx == 8) {
								alertlabel = alertlabel + 1;
							} else {
								alertlabel = alertlabel + 2;
							}
						}
					}
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
					if (alertnamecount + 21 < alertlength - 1 && !(alertdata[alertnamecount + 21].equals("日付"))) {
						alertnamecount = alertnamecount + 21;
						alertlabel = alertlabel + 3;
					} else {
						break;
					}
				}
				if (weatherflg && !(alertfirst)) {
					alertnamecount = alertnamecount + 26;
					alertlabel = alertlabel + 8;
				}
				//2段目のデータを取得する（データが存在する場合)
				while (weatherflg) {
					Weather_alertData data = new Weather_alertData();
					name = alertdata[alertnamecount];
					alertnamecount++;
					while (!(alertdata[alertnamecount].matches(".*日"))) {
						name = name + " " + alertdata[alertnamecount];
						alertnamecount++;
						alertlabel++;
					}
					alertnamecount = alertnamecount - 1;
					data.setName(name);
					List<String> dayalertlist = new ArrayList<String>();
					List<String> dayalertclasslist = new ArrayList<String>();
					for(int idx=0; idx<4; idx++) {
						dayalertlist.add(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						dayalertclasslist.add("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						dayalertclasslist.add("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						dayalertclasslist.add("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setDayalertdata1class("black");
					}
					if(!(idx == 3)) {
						alertlabel = alertlabel + 2;
					}
					}

					data.setAlert1(dayalertlist.get(0));
					data.setDayalertdata1class(dayalertclasslist.get(0));
					data.setAlert2(dayalertlist.get(1));
					data.setDayalertdata2class(dayalertclasslist.get(1));
					data.setAlert3(dayalertlist.get(2));
					data.setDayalertdata3class(dayalertclasslist.get(2));
					data.setAlert4(dayalertlist.get(3));
					data.setDayalertdata4class(dayalertclasslist.get(3));
					entity.getWeather_alert2List().add(data);
					if (alertnamecount + 9 > alertlength - 1) {
						alertnamecount = alertnamecount + 9;
						alertlabel = alertlabel + 3;
						break;
					} else {
					}
				}

			}
		} catch (
		IOException e) {

			entity.setError(true);
			return entity;

		}
		return entity;
	}
}