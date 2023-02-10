package jp.ac.hcs.mbraw.horoscope;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class HoroscopeEntity {
	/** 占い情報リスト */
	private final List<HoroscopeData> horoscopeList = new ArrayList<>();

	public static HoroscopeEntity error() {
		final HoroscopeEntity horoscopeEntity = new HoroscopeEntity();
		final HoroscopeData horoscopeData = new HoroscopeData();

		horoscopeData.setCatchflg(true);
		horoscopeData.setTop("エラーが発生しました");

		horoscopeEntity.getHoroscopeList().add(horoscopeData);

		return horoscopeEntity;
	}
}
