package jp.ac.hcs.morning.horoscope;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.hcs.morning.HttpConnectUtils;

@Service
public class HoroscopeService {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	/** 日付の取得を行いyyyy/MM/dd形式の文字列を返す */
	private static final String TODAY = SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime());

	/** エンドポイント */
	private static final String API = "http://api.jugemkey.jp/api/horoscope/free/";

	/**
	 * 占いの順位の昇順にデータを取得するサービス
	 *
	 * @return entity
	 */
	public HoroscopeEntity getHoroscopeData() {
		final String horoscopeUrl = API + TODAY;
		final String data = HttpConnectUtils.connectAndGetText(horoscopeUrl);
		// 結果をデータに変換
		final HoroscopeEntity entity = data.isEmpty() ? HoroscopeEntity.error() : this.convert(data);

		return entity;
	}

	/**
	 * 星座情報を昇順に並び変えて返すプログラム
	 *
	 * @param json
	 * @param date
	 * @return
	 */
	private HoroscopeEntity convert(String json) {

		JsonNode horoscope = null;

		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			horoscope = objectMapper.readValue(json, JsonNode.class);

		}
		catch (IOException e) {
			// 変換に失敗や接続ができなかった場合、エラー専用のオブジェクトを返す
			e.printStackTrace();
			return HoroscopeEntity.error();
		}

		// 正常に変換できたのでJsonNodeからデータを取得して各種値を設定する
		final HoroscopeEntity entity = new HoroscopeEntity();

		final JsonNode horoscopeToday = horoscope.get("horoscope").get(TODAY);

		for (int idx = 0; idx < 12; idx++) {
			final JsonNode horosort = horoscopeToday.get(idx);
			final HoroscopeData data = new HoroscopeData();

			data.setContent(horosort.get("content").asText());
			data.setLucky_item(horosort.get("item").asText());
			data.setMoney(horosort.get("money").asText());
			data.setTotal(horosort.get("total").asText());
			data.setJob(horosort.get("job").asText());
			data.setColor(horosort.get("color").asText());
			data.setLove(horosort.get("love").asText());
			data.setRank(horosort.get("rank").asInt());
			data.setSign(horosort.get("sign").asText());

			entity.getHoroscopeList().add(data);
		}

		entity.getHoroscopeList().sort((d1, d2) -> {
			return d1.getRank() - d2.getRank();
		});

		return entity;
	}

}
