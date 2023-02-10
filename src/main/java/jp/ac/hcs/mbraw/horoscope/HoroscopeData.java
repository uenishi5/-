package jp.ac.hcs.mbraw.horoscope;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString
public class HoroscopeData {

	/** アドバイス（詳細) */
	private String content;

	/** ラッキーアイテム */
	private String lucky_item;

	/** ラッキーカラー */
	private String color;

	/** 順位 */
	private int rank;

	/** 画像パス */
	@Setter(value = AccessLevel.PRIVATE)
	private String photopath;

	/** １位の星座 */
	private String top;

	/** 星座名 */
	private String sign;

	/** 金運（５段階評価） */
	private String money;

	/** 仕事運（５段階評価） */
	private String job;

	/** 恋愛運（５段階評価） */
	private String love;

	/** 総合運（５段階評価） */
	private String total;

	/** エラーキャッチフラグ */
	private boolean catchflg;

	/**
	 * 星座の名称と画像を設定するメソッド
	 *
	 * @param sign
	 */
	public void setSign(String sign) {
		this.sign = sign;
		this.setPhotopath(getSignResourcePath(sign));
	}

	public static String getSignResourcePath(String sign) {
		switch (sign) {
			case "牡羊座":
				return ("morning/ohitsuji.png");
			case "乙女座":
				return ("morning/otome.png");
			case "天秤座":
				return ("morning/tenbin.png");
			case "双子座":
				return ("morning/futago.png");
			case "牡牛座":
				return ("morning/oushi.png");
			case "獅子座":
				return ("morning/shishi.png");
			case "蠍座":
				return ("morning/sasori.png");
			case "蟹座":
				return ("morning/kani.png");
			case "射手座":
				return ("morning/ite.png");
			case "山羊座":
				return ("morning/yagi.png");
			case "水瓶座":
				return ("morning/mizugame.png");
			case "魚座":
				return ("morning/uo.png");

		}
		return ("morning/uo.png");
	}

	public static HoroscopeData error() {
		final HoroscopeData horoscopeData = new HoroscopeData();

		horoscopeData.setPhotopath("morning/uo.png");
		horoscopeData.setTop("取得エラー");

		return horoscopeData;
	}
}
