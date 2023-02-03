package jp.ac.hcs.morning.horoscope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HoroscopeService {

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	/** エンドポイント */
	private static final String API = "http://api.jugemkey.jp/api/horoscope/free/";

	/**
	 * 占いの順位の昇順にデータを取得するサービス
	 *
	 * @return entity
	 */
	public HoroscopeEntity getHoroscopeData() {
		final String today = this.getToday();
		final String horoscopeUrl = API + today;

		String result = "";

		// URLが正常なものか確認するための try-catch 文
		URL url = null;
		try {
			url = new URL(horoscopeUrl);
		}
		catch (MalformedURLException e) {
			// URL解析した結果エラーが発生した場合、エラー専用のHoroscopeEntityオブジェクトを返す
			e.printStackTrace();
			return HoroscopeEntity.error();
		}

		HttpURLConnection connection = null;

		// APIへリクエスト送信
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
		}
		catch (IOException e) {
			// 接続失敗した場合、エラー専用のHoroscopeEntityオブジェクトを返す
			e.printStackTrace();
			return HoroscopeEntity.error();
		}

		String tmp = "";
		try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			while ((tmp = in.readLine()) != null) {
				result += tmp;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			// データ取得最中に異常が発生した場合、エラー専用のHoroscopeEntityオブジェクトを返す
			return HoroscopeEntity.error();
		}

		// 結果をデータに変換
		final HoroscopeEntity entity = this.convert(result, today);

		connection.disconnect();

		return entity;
	}

	/**
	 * 星座情報を昇順に並び変えて返すプログラム
	 *
	 * @param json
	 * @param date
	 * @return
	 */
	private HoroscopeEntity convert(String json, String date) {

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

		final JsonNode horoscopeToday = horoscope.get("horoscope").get(date);

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

		return entity;
	}

	/** 日付の取得を行いyyyy/MM/dd形式の文字列を返す */
	private String getToday() {
		return SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime());
	}
}
