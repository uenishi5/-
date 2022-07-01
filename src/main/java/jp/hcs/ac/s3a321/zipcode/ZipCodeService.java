package jp.hcs.ac.s3a321.zipcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.hcs.ac.s3a321.zipcode.pojo.ZipCodePojo;

/**
 * 郵便番号APIの業務ロジックを実現するクラスです。
 * <p>
 * 処理の継続ができなくなった場合は、 呼び出し元に例外処理をさせずにメソッド内で例外処理を行いエラーメッセージを設定します。
 * <strong>呼び出し元にnullが返却されることはありません。</strong>
 *
 * @author s20203029
 *
 */

@Service
@Transactional
public class ZipCodeService {

	@Autowired
	private RestTemplate restTemplate;

	/** エンドポイント */
	private static final String URL = "http://zipcloud.ibsnet.co.jp/api/search?zipcode={zipcode}";

	/**
	 * 郵便番号から検索結果形式に変換します。
	 * <p>
	 * 実行に応じた
	 *
	 * @param zipcode 郵便番号の文字列（7桁）を格納(null不可)
	 */
	public ZipCodeEntity execute(ZipCodeForm zipCodeForm) {
		// インスタンスの生成
		ZipCodeEntity zipCodeEntity = new ZipCodeEntity();
		// APIへリクエスト送信
		String json = this.request(zipCodeForm.getZipCode());
		// 結果をデータに変換
		this.convert(json, zipCodeEntity);
		return zipCodeEntity;
	}

	private void convert(String json, ZipCodeEntity zipCodeEntity) {
		ZipCodePojo zipCodePojo = this.extracted(json);
		zipCodeEntity.setStatus(zipCodePojo.status);
		zipCodePojo.results.stream().map(result -> {
			ZipCodeData zipCodeData = new ZipCodeData();
			zipCodeData.setZipCode(result.zipcode);
			zipCodeData.setPrefCode(result.prefcode);
			zipCodeData.setAddress1(result.address1);
			zipCodeData.setAddress2(result.address2);
			zipCodeData.setAddress3(result.address3);
			zipCodeData.setKana1(result.kana1);
			zipCodeData.setKana2(result.kana2);
			zipCodeData.setKana3(result.kana3);
			return zipCodeData;
		}).iterator().forEachRemaining(zipCodeEntity.getList()::add);
	}

	/**
	 * json形式をjavaで扱えるようにするメソッドです。
	 *
	 * @param json jsonデータ
	 * @return エラーが発生した場合、空のデータを渡します。
	 */
	private ZipCodePojo extracted(String json) {
		try {
			return new ObjectMapper().readValue(json, ZipCodePojo.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ZipCodePojo.empty();
		}
	}

	private String request(String zipcode) {
		String json = this.restTemplate.getForObject(ZipCodeService.URL, String.class, zipcode);
		return json;
	}
}
