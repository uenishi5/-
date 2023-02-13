package jp.ac.hcs.mbraw.horoscope;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.hcs.mbraw.HttpConnectUtils;
import jp.ac.hcs.mbraw.horoscope.pojo.HoroscopeObject;

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

		HoroscopeObject horoscopeObject = null;

		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			horoscopeObject = objectMapper.readValue(json, HoroscopeObject.class);

		}
		catch (IOException e) {
			// 変換に失敗や接続ができなかった場合、エラー専用のオブジェクトを返す
			e.printStackTrace();
			return HoroscopeEntity.error();
		}

		// 正常に変換できたのでJsonNodeからデータを取得して各種値を設定する
		final HoroscopeEntity entity = new HoroscopeEntity();

		final List<jp.ac.hcs.mbraw.horoscope.pojo.HoroscopeData> horoscopeToday = horoscopeObject.getHoroscope().get(TODAY);

		final List<HoroscopeData> horoscopes = horoscopeToday.stream().map(horosort -> {
			final HoroscopeData data = new HoroscopeData();

			data.setContent(horosort.getContent());
			data.setLucky_item(horosort.getItem());
			data.setMoney(horosort.getMoney());
			data.setTotal(horosort.getTotal());
			data.setJob(horosort.getJob());
			data.setColor(horosort.getColor());
			data.setLove(horosort.getLove());
			data.setRank(horosort.getRank());
			data.setSign(horosort.getSign());

			return data;
		}).toList();

		entity.getHoroscopeList().addAll(horoscopes);

		entity.getHoroscopeList().sort((d1, d2) -> {
			return d1.getRank() - d2.getRank();
		});

		return entity;
	}

}
