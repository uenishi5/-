package jp.ac.hcs.mbraw.traffic;

import java.util.List;

import com.google.api.client.util.Lists;

import lombok.Data;
@Data
public class TrafficData {
	
	private static int TRAFFIC_DATA_SIZE = 6;
	
	/** 通行止めなど、障害が起きているかのフラグ*/
	private boolean alertflg ;
	
	/** アラートが出ている高速バスの場所名 */
	private String alertbus;
	
	/** バスの路線名 */
	private String rosen;
	
	/** バス方面 */
	private String houmen;
	
	/** バス運行状況 */
	private String situation;
	
	/** バス備考欄 */
	private String remarks;
	
	/** 要素（行）数 */
	private int rowspan;
	
	/** 高速道路の上りor下り情報*/
	private String highwayroad;
	
	/** 高速道路詳細情報 */
	private String highwaydata;
	
	/** 路線連番*/
	private int count;
	
	/** 各方面の情報有無のフラグリスト*/
	private final List<TrafficflgData> trafficFlgList = Lists.newArrayListWithCapacity(TRAFFIC_DATA_SIZE);
	
	@Data
	public static class TrafficflgData {
		/** 各方面のフラグ */
		private boolean trafficflg;
	}
	
	/** エラーキャッチフラグ*/
	private boolean catchflg;
	
	/** 空のTrafficDataオブジェクトを返す */
	public static TrafficData empty() {
		final TrafficData data = new TrafficData();

		for (int i = 0; i < TRAFFIC_DATA_SIZE; i++) {
			data.trafficFlgList.add(new TrafficflgData());
		}


		return data;
	}
}
