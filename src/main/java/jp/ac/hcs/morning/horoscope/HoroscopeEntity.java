package jp.ac.hcs.morning.horoscope;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class HoroscopeEntity {
	/** 占い情報リスト */
	private List<HoroscopeData> horoscopeList = new ArrayList<>();

	public static HoroscopeEntity error() {
		final HoroscopeEntity horoscopeEntity = new HoroscopeEntity();
		final HoroscopeData horoscopeData = new HoroscopeData();

		horoscopeData.setContent("error");

		horoscopeEntity.getHoroscopeList().add(horoscopeData);

		return horoscopeEntity;
	}
}
