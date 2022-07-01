package jp.hcs.ac.s3a321.zipcode;

import lombok.Data;
import lombok.ToString;

/**
 * 1件分の郵便番号情報を管理するクラス。
 *
 * <p>
 * レスポンスフィールドのresults内のデータを管理します。
 *
 * @author s20203029
 *
 */
@Data
@ToString
public class ZipCodeData {
	/** 郵便番号 */
	private String zipCode;

	/** 都道府県コード */
	private String prefCode;

	/** 都道府県名 */
	private String address1;

	/** 市区町村名 */
	private String address2;

	/** 町域名 */
	private String address3;

	/** 都道府県名カナ */
	private String kana1;

	/** 市区町村名カナ */
	private String kana2;

	/** 町域名カナ */
	private String kana3;
}
