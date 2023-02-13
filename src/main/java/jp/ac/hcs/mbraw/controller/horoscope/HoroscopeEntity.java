package jp.ac.hcs.mbraw.controller.horoscope;

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

		horoscopeEntity.getHoroscopeList().add(horoscopeData);

		return horoscopeEntity;
	}
}
