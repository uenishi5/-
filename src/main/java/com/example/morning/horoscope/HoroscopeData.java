package com.example.morning.horoscope;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HoroscopeData {
	
	/** アドバイス（詳細) */
	private String content;
	
	/** ラッキーアイテム */
	private String lucky_item;
	
	/** ラッキーカラー*/
	private String color;
	
	/** 順位 */
	private int rank;
	
	/** 画像パス */
	private String photopath;
	
	/** １位の星座 */
	private String top;
	
	/** 星座名 */
	private String sign;
	
	/** 金運（５段階評価） */
	private String money;
	
	/** 仕事運（５段階評価）*/
	private String job;
	
	/** 恋愛運（５段階評価）*/
	private String love;
	
	/** 総合運（５段階評価）*/
	private String total;
	
	/** エラーキャッチフラグ*/
	private boolean catchflg;
}
