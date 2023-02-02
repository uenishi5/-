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
	/** エンドポイント */
	private static final String API = "http://api.jugemkey.jp/api/horoscope/free/";

	/**
	 * 占いの順位の昇順にデータを取得するサービス
	 *
	 * @return entity
	 */
	public HoroscopeEntity getHoroscopeData() {
		final String date = this.simpleDateFormat();
		final String horoscopeUrl = API + date;

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
		final HoroscopeEntity entity = this.convert(result, date);

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
		final ObjectMapper objectMapper = new ObjectMapper();

		final HoroscopeEntity entity = new HoroscopeEntity();
		String[][] horosortlist = new String[12][9];

		try {
			final JsonNode horoscope = objectMapper.readValue(json, JsonNode.class);
			for (int idx = 0; idx < 12; idx++) {
				final JsonNode horosort = horoscope.get("horoscope").get(date).get(idx);
				int rank = horosort.get("rank").asInt() - 1;
				horosortlist[rank][0] = horosort.get("content").asText();
				horosortlist[rank][1] = horosort.get("item").asText();
				horosortlist[rank][2] = horosort.get("money").asText();
				horosortlist[rank][3] = horosort.get("total").asText();
				horosortlist[rank][4] = horosort.get("job").asText();
				horosortlist[rank][5] = horosort.get("color").asText();
				horosortlist[rank][6] = horosort.get("love").asText();
				horosortlist[rank][7] = horosort.get("rank").asText();
				horosortlist[rank][8] = horosort.get("sign").asText();

				HoroscopeData data = new HoroscopeData();
				data.setContent(horosortlist[idx][0]);
				data.setLucky_item(horosortlist[idx][1]);
				data.setMoney(horosortlist[idx][2]);
				data.setTotal(horosortlist[idx][3]);
				data.setJob(horosortlist[idx][4]);
				data.setColor(horosortlist[idx][5]);
				data.setLove(horosortlist[idx][6]);
				String rank = horosortlist[idx][7];
				data.setRank(Integer.parseInt(rank));
				data.setSign(horosortlist[idx][8]);
				String sign = horosortlist[idx][8];
				switch (data.getSign()) {
					case "牡羊座":
						data.setPhotopath("morning/ohitsuji.png");
						break;
					case "乙女座":
						data.setPhotopath("morning/otome.png");
						break;
					case "天秤座":
						data.setPhotopath("morning/tenbin.png");
						break;
					case "双子座":
						data.setPhotopath("morning/futago.png");
						break;
					case "牡牛座":
						data.setPhotopath("morning/oushi.png");
						break;
					case "獅子座":
						data.setPhotopath("morning/shishi.png");
						break;
					case "蠍座":
						data.setPhotopath("morning/sasori.png");
						break;
					case "蟹座":
						data.setPhotopath("morning/kani.png");
						break;
					case "射手座":
						data.setPhotopath("morning/ite.png");
						break;
					case "山羊座":
						data.setPhotopath("morning/yagi.png");
						break;
					case "水瓶座":
						data.setPhotopath("morning/mizugame.png");
						break;
					case "魚座":
						data.setPhotopath("morning/uo.png");
						break;

				}
				entity.getHoroscopeList().add(data);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return entity;
	}

	/** 日付の取得を行う */
	private String simpleDateFormat() {
		Calendar cl = Calendar.getInstance();

		// 日付をyyyy/MM/ddの形で出力する
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		String str1 = sdf1.format(cl.getTime());
		return str1;
	}
}
