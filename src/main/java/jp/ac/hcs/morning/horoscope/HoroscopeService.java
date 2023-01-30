package jp.ac.hcs.morning.horoscope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
	 * @return entity
	 */
	public HoroscopeEntity getHoroscopeData() {
		HoroscopeEntity entity = new HoroscopeEntity();
		String result = "";

		URL url;
		try {
			String date = simpleDateFormat();
			String horoscopeurl = API + date;
			url = new URL(horoscopeurl);
			// APIへリクエスト送信
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String tmp = "";

			while ((tmp = in.readLine()) != null) {
				result += tmp;
			}

			// 結果をデータに変換

			entity = this.convert(result, date);
			in.close();
			connection.disconnect();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return entity;
	}

//	/**
//	 * 特定の星座の詳細を返すサービス
//	 * @param sign 星座名
//	 * @return
//	 */
//	public HoroscopeEntity getHoroscopeDetail(String sign) {
//		HoroscopeEntity entity = new HoroscopeEntity();
//		String result = "";
//
//		URL url;
//		try {
//			String date = simpleDateFormat();
//			String horoscopeurl = API + date;
//			url = new URL(horoscopeurl);
//			// APIへリクエスト送信
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.connect();
//			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			String tmp = "";
//
//			while ((tmp = in.readLine()) != null) {
//				result += tmp;
//			}
//
//			// 結果をデータに変換
//			entity = this.convert_only(result, date, sign);
//			in.close();
//			connection.disconnect();
//		} catch (Exception e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
//
//		return entity;
//	}

	/**
	 * 星座情報を昇順に並び変えて返すプログラム
	 * @param json
	 * @param date
	 * @return
	 */
	private HoroscopeEntity convert(String json, String date) {
		ObjectMapper objectMapper = new ObjectMapper();

		HoroscopeEntity entity = new HoroscopeEntity();
		String[][] horosortlist = new String[12][9];

		try {
			JsonNode horoscope = objectMapper.readValue(json, JsonNode.class);
			for (int idx = 0; idx < 12; idx++) {
				int rank = horoscope.get("horoscope").get(date).get(idx).get("rank").asInt();
				rank = rank - 1;
				horosortlist[rank][0] = horoscope.get("horoscope").get(date).get(idx).get("content").asText();
				horosortlist[rank][1] = horoscope.get("horoscope").get(date).get(idx).get("item").asText();
				horosortlist[rank][2] = horoscope.get("horoscope").get(date).get(idx).get("money").asText();
				horosortlist[rank][3] = horoscope.get("horoscope").get(date).get(idx).get("total").asText();
				horosortlist[rank][4] = horoscope.get("horoscope").get(date).get(idx).get("job").asText();
				horosortlist[rank][5] = horoscope.get("horoscope").get(date).get(idx).get("color").asText();
				horosortlist[rank][6] = horoscope.get("horoscope").get(date).get(idx).get("love").asText();
				horosortlist[rank][7] = horoscope.get("horoscope").get(date).get(idx).get("rank").asText();
				horosortlist[rank][8] = horoscope.get("horoscope").get(date).get(idx).get("sign").asText();
			}
			for (int idx = 0; idx < 12; idx++) {
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
				if (sign.equals("牡羊座")) {
					data.setPhotopath("morning/ohitsuji.png");
				} else if (sign.equals("乙女座")) {
					data.setPhotopath("morning/otome.png");
				} else if (sign.equals("天秤座")) {
					data.setPhotopath("morning/tenbin.png");
				} else if (sign.equals("双子座")) {
					data.setPhotopath("morning/futago.png");
				} else if (sign.equals("牡牛座")) {
					data.setPhotopath("morning/oushi.png");
				} else if (sign.equals("獅子座")) {
					data.setPhotopath("morning/shishi.png");
				} else if (sign.equals("蠍座")) {
					data.setPhotopath("morning/sasori.png");
				} else if (sign.equals("蟹座")) {
					data.setPhotopath("morning/kani.png");
				} else if (sign.equals("射手座")) {
					data.setPhotopath("morning/ite.png");
				} else if (sign.equals("山羊座")) {
					data.setPhotopath("morning/yagi.png");
				} else if (sign.equals("水瓶座")) {
					data.setPhotopath("morning/mizugame.png");
				} else if (sign.equals("魚座")) {
					data.setPhotopath("morning/uo.png");
				}else {
					data.setPhotopath("null");
				}
				entity.getHoroscopeList().add(data);
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return entity;
	}

//	/** 一件のデータ取得 */
//
//	private HoroscopeEntity convert_only(String json, String date, String sign) {
//		ObjectMapper objectMapper = new ObjectMapper();
//
//		HoroscopeEntity entity = new HoroscopeEntity();
//
//		try {
//			JsonNode horoscope = objectMapper.readValue(json, JsonNode.class);
//			for (int idx = 0; idx < 12; idx++) {
//
//				HoroscopeData data = new HoroscopeData();
//				String getsign = horoscope.get("horoscope").get(date).get(idx).get("sign").asText();
//				if (getsign.equals(sign)) {
//					data.setContent(horoscope.get("horoscope").get(date).get(idx).get("content").asText());
//					data.setLucky_item(horoscope.get("horoscope").get(date).get(idx).get("item").asText());
//					data.setMoney(horoscope.get("horoscope").get(date).get(idx).get("money").asText());
//					data.setTotal(horoscope.get("horoscope").get(date).get(idx).get("total").asText());
//					data.setJob(horoscope.get("horoscope").get(date).get(idx).get("job").asText());
//					data.setColor(horoscope.get("horoscope").get(date).get(idx).get("color").asText());
//					data.setLove(horoscope.get("horoscope").get(date).get(idx).get("love").asText());
//					data.setRank(horoscope.get("horoscope").get(date).get(idx).get("rank").asInt());
//					data.setSign(horoscope.get("horoscope").get(date).get(idx).get("sign").asText());
//					entity.getHoroscopeList().add(data);
//					break;
//				}
//			}
//		} catch (IOException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
//		return entity;
//	}
/** 日付の取得を行う */
	private String simpleDateFormat() {
		Calendar cl = Calendar.getInstance();

		//日付をyyyy/MM/ddの形で出力する
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		String str1 = sdf1.format(cl.getTime());
		return str1;
	}
}
