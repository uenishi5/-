package jp.hcs.ac.s3a321.zipcode;

import java.util.ArrayList;
import java.util.List;

import jp.hcs.ac.s3a321.main.MessageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 郵便番号検索レスポンスです。
 * <p>
 * 各項目のデータ仕様については、APIを仕様を参照してください。 １つの郵便番号に複数の住所が紐づく可能性があるため、リスト構造となっています。
 *
 * @author s20203029
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZipCodeEntity extends MessageEntity {

	/** ステータス */
	private int status;

	/** 郵便番号情報リスト */
	private List<ZipCodeData> list = new ArrayList<>();
}
