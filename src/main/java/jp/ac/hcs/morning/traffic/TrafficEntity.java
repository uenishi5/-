package jp.ac.hcs.morning.traffic;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TrafficEntity {

	private List<TrafficData> trafficList = new ArrayList<>();
	/** 路線リスト */
	private List<TrafficData> trafficrosenList = new ArrayList<>();

	/** 方面、運行、備考 */
	private List<TrafficData> trafficanotherList = new ArrayList<>();

	/** フラグ */
	private List<TrafficData> trafficflgList = new ArrayList<>();

	public static TrafficEntity error() {
		final TrafficEntity trafficEntity = new TrafficEntity();
		final TrafficData trafficData = new TrafficData();

		trafficData.setAlertflg(true);

		trafficEntity.getTrafficflgList().add(trafficData);

		return trafficEntity;
	}
}
