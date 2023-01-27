package jp.ac.hcs.morning.weather_alert;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Weather_alertEntity {
	private List<Weather_alertData> weather_alertnameList = new ArrayList<>();
	
	private List<Weather_alertData> weather_alertList = new ArrayList<>();
	
	private List<Weather_alertData> weather_alert2List = new ArrayList<>();
	
	private List<Weather_alertData> weather_dateList = new ArrayList<>();
	
	private boolean error ;
}
