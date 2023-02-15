package jp.ac.hcs.mbraw.controller.traffic;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TrafficEntity {
	
	/** メイン画面で使用するフラグ */
    private List<TrafficData> trafficList = new ArrayList<>();
    
	/** 路線 方面、運行、備考を格納するリスト */
	private List<TrafficData> trafficrosenList = new ArrayList<>();

	/** 一覧に表示する際に使用するフラグリスト */
	private List<TrafficData> trafficflgList = new ArrayList<>();
    /**未使用*/
//	/** 方面、運行、備考 */
//	private List<TrafficData> trafficanotherList = new ArrayList<>();
	
//	/** 交通画面情報有無フラグリスト */
//	private List<TrafficData> trafficdataflgList = new ArrayList<>();

	public static TrafficEntity error() {
		final TrafficEntity trafficEntity = new TrafficEntity();
		final TrafficData trafficData = new TrafficData();

		trafficData.setAlertflg(true);

		trafficEntity.getTrafficflgList().add(trafficData);

		return trafficEntity;
	}
}
