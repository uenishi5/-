package jp.ac.hcs.morning.weather_alert;

import lombok.Data;

@Data
public class Weather_alertData {

	/** メイン画面に表示する警報内容 */
	private String alertname;

	/** 警報、注意報の数*/
	private int alertcount;

	/** 警報、注意報の内容*/
	private String name;

	/** １つ目の時間情報 */
	private String date1;

	/** ２つ目の情報 */
	private String date2;

	/** ３つ目の情報 */
	private String date3;

	/** ４つ目の情報 */
	private String date4;

	/** ５つ目の情報 */
	private String date5;

	/** ６つ目の情報 */
	private String date6;

	/** ７つ目の情報 */
	private String date7;

	/** ８つ目の情報 */
	private String date8;

	/**９つ目の情報 */
	private String date9;

	/** １０個目の情報 */
	private String date10;

	/** 1～１０の警報情報 */
	private String alertdata1;
	private String alertdata2;
	private String alertdata3;
	private String alertdata4;
	private String alertdata5;
	private String alertdata6;
	private String alertdata7;
	private String alertdata8;
	private String alertdata9;
	private String alertdata10;
	
	/** 1～１０の埋め込み用クラス(white,yellow,red,black) */
	private String alertdata1class;
	private String alertdata2class;
	private String alertdata3class;
	private String alertdata4class;
	private String alertdata5class;
	private String alertdata6class;
	private String alertdata7class;
	private String alertdata8class;
	private String alertdata9class;
	private String alertdata10class;

	/** 今日の警報情報(なだれ等) */
	private String alert1;
	private String alertdaydate1;

	/** 明日の警報情報 */
	private String alert2;
	private String alertdaydate2;
	
	/** 明後日の警報情報 */
	private String alert3;
	private String alertdaydate3;

	/** 明々後日の警報情報 */
	private String alert4;
	private String alertdaydate4;
	
	/** １～４の埋め込み用クラス(white,yellow,red,black)*/
	private String dayalertdata1class;
	private String dayalertdata2class;
	private String dayalertdata3class;
	private String dayalertdata4class;

	/** 注意報,警報、緊急警報の色 */
	private String alert_color;

	/** エラーキャッチフラグ*/
	private boolean catchflg;
}
