package com.example.morning.horoscope;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
@Data
public class HoroscopeEntity {	
		/** 占い情報リスト */
		private List<HoroscopeData> horoscopeList = new ArrayList<>();
}
