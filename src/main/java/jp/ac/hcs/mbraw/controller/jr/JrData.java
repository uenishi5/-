package jp.ac.hcs.mbraw.controller.jr;

import lombok.Data;

@Data
public class JrData {
	// 内容
	private String content;
	// 警報名
	private String alertname;
	// 文字の色
	private String color;
	// 名前
	private String title;

	public static JrData error() {
		final JrData jrData = new JrData();

		jrData.setContent("エラー");
		jrData.setColor("red");

		return jrData;
	}
}
