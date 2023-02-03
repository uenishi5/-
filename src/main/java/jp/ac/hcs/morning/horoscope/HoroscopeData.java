package jp.ac.hcs.morning.horoscope;

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

		switch (sign) {
			case "牡羊座":
				this.setPhotopath("morning/ohitsuji.png");
				break;
			case "乙女座":
				this.setPhotopath("morning/otome.png");
				break;
			case "天秤座":
				this.setPhotopath("morning/tenbin.png");
				break;
			case "双子座":
				this.setPhotopath("morning/futago.png");
				break;
			case "牡牛座":
				this.setPhotopath("morning/oushi.png");
				break;
			case "獅子座":
				this.setPhotopath("morning/shishi.png");
				break;
			case "蠍座":
				this.setPhotopath("morning/sasori.png");
				break;
			case "蟹座":
				this.setPhotopath("morning/kani.png");
				break;
			case "射手座":
				this.setPhotopath("morning/ite.png");
				break;
			case "山羊座":
				this.setPhotopath("morning/yagi.png");
				break;
			case "水瓶座":
				this.setPhotopath("morning/mizugame.png");
				break;
			case "魚座":
				this.setPhotopath("morning/uo.png");
				break;

		}
	}
}
