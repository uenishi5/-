package jp.ac.hcs.config;

public class Mapping {
	public static final String MAPPING_ROOT = "/";
	public static final String MAPPING_NEWSAPI = MAPPING_ROOT + "newsapi";
	public static final String MAPPING_PORNHUBAPI = MAPPING_ROOT + "pornhubapi";
	public static final String MAPPING_YOUTUBEAPI = MAPPING_ROOT + "youtubeapi";
	public static final String MAPPING_YOUTUBE_DL = MAPPING_ROOT + "youtube-dl";

	
	// -----directory-----

	public static final String DIRECTORY_NEWSAPI = "newsapi/";
	public static final String DIRECTORY_MORNING = "morning/";
	
	// -----html-----

	public static final String RESOURCE_INDEX = "index";
	public static final String RESOURCE_MAIN = DIRECTORY_MORNING +"main";
	public static final String RESOURCE_CHART = DIRECTORY_MORNING +"chart";
	public static final String RESOURCE_HOROSCOPE = DIRECTORY_MORNING +"divination";
	public static final String RESOURCE_JR = DIRECTORY_MORNING +"jr_traffic";
	public static final String RESOURCE_TRAFFIC = DIRECTORY_MORNING + "traffic";
	public static final String RESOURCE_TRAFFIC_RESULT = DIRECTORY_MORNING + "traffic_result";
	public static final String RESOURCE_WEATHER = DIRECTORY_MORNING + "weather";
	public static final String RESOURCE_WEATHER_ALERT = DIRECTORY_MORNING + "alert";



}
