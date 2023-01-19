package com.example.morning.weather_alert;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
// 警報注意報を取得するクラス
@Service
public class Weather_alertService {
	// 警報注意報を取得するメソッド
	public Weather_alertEntity getWeather_alertData() {
		Document document;
		Weather_alertEntity entity = new Weather_alertEntity();
		entity.setError(false);
		try {
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
					data.setAlertdata1(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata1class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata1class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata1class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata1class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlertdata2(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata2class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata2class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata2class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata2class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlertdata3(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata3class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata3class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata3class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata3class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlertdata4(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata4class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata4class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata4class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata4class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlertdata5(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata5class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata5class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata5class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata5class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlertdata6(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata6class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata6class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata6class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata6class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlertdata7(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata7class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata7class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata7class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata7class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlertdata8(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata8class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata8class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata8class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata8class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlertdata9(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata9class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata9class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata9class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata9class("black");
					}
					alertlabel = alertlabel + 1;
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setAlertdata10class("white");
						data.setAlertdata10(alertdata[alertlabel]);
						errorflg = true;
						alertlabel = alertlabel + 1;
						alertnamecount = alertnamecount - 1;
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setAlertdata10class("yellow");
						data.setAlertdata10(alertdata[alertlabel]);
						errorflg = true;
						alertlabel = alertlabel + 1;
						alertnamecount = alertnamecount - 1;
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setAlertdata10class("red");
						data.setAlertdata10(alertdata[alertlabel]);
						errorflg = true;
						alertlabel = alertlabel + 1;
						alertnamecount = alertnamecount - 1;
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setAlertdata10class("black");
						data.setAlertdata10(alertdata[alertlabel]);
						errorflg = true;
						alertlabel = alertlabel + 1;
						alertnamecount = alertnamecount - 1;
					} else {
						alertlabel = alertlabel + 1;
					}
					if (!(errorflg)) {
						data.setAlertdata10(alertdata[alertlabel]);
						if (alertdata[alertlabel].equals("発表なし")) {
							data.setAlertdata10class("white");
						} else if (alertdata[alertlabel].equals("注意報級")) {
							data.setAlertdata10class("yellow");
						} else if (alertdata[alertlabel].equals("警報級")) {
							data.setAlertdata10class("red");
						} else if (alertdata[alertlabel].equals("特別警報級")) {
							data.setAlertdata10class("black");
						}
					}
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
					data.setAlert1(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setDayalertdata1class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setDayalertdata1class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setDayalertdata1class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setDayalertdata1class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlert2(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setDayalertdata2class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setDayalertdata2class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setDayalertdata2class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setDayalertdata2class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlert3(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setDayalertdata3class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setDayalertdata3class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setDayalertdata3class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setDayalertdata3class("black");
					}
					alertlabel = alertlabel + 2;
					data.setAlert4(alertdata[alertlabel]);
					if (alertdata[alertlabel].equals("発表なし")) {
						data.setDayalertdata4class("white");
					} else if (alertdata[alertlabel].equals("注意報級")) {
						data.setDayalertdata4class("yellow");
					} else if (alertdata[alertlabel].equals("警報級")) {
						data.setDayalertdata4class("red");
					} else if (alertdata[alertlabel].equals("特別警報級")) {
						data.setDayalertdata4class("black");
					}
					entity.getWeather_alert2List().add(data);
					if (alertnamecount + 9 > alertlength - 1) {
						alertnamecount = alertnamecount + 9;
						alertlabel = alertlabel + 3;
						break;
					} else {
					}
				}

			}
		} catch (IOException e) {
			
			entity.setError(true);
			return entity;

		}
		return entity;
	}
}