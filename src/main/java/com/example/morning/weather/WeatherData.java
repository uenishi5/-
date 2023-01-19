package com.example.morning.weather;

import lombok.Data;
import lombok.ToString;

/**
 * レスポンスフィールドのresults内のデータを管理します。
 */
@Data
@ToString
public class WeatherData {
	/** 予報日 */
	private String date;

	/** 予報日（今日・明日・明後日のいずれか） */
	private String dateLabel;

	/** 天気（晴れ、曇り、雨など） */
	private String telop;

	/** 詳細な天気情報 */
	private String weather;

	/** 風の強さ */
	private String wind;

	/** 波の高さ（海に面している地域のみ） */
	private String wave;

	/** 最高気温 */
	private String temperature_max;
	
	/** 最低気温 */
	private String temperature_min;
	
    /** 降水確率 */
	/** 0 時から 6 時までの降水確率 */
	private String chanceOfRain_T00_06;
	
	/** 6 時から 12 時までの降水確率 */
	private String chanceOfRain_T06_12;
	
	/** 12 時から 18 時までの降水確率 */
	private String chanceOfRain_T12_18;
	
	/** 18 時から 24 時までの降水確率 */
	private String chanceOfRain_T18_24;
	
	/** 地方名 */
	private String area;
	
	/** 都道府県名 */
	private String prefecture;
	
	/** 地域名 */
	private String city;
	
	/** 画像データ */
	private String svg;
	
	/** エラーキャッチフラグ*/
	private boolean catchflg;
	
}
