package jp.ac.hcs.morning.jr;

import lombok.Data;

@Data
public class JrData {
	//内容
	private String content;
	//警報名
	private String alertname;
	//文字の色
	private String color;
	//名前
	private String title;
}
