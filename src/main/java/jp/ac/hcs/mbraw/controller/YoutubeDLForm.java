package jp.ac.hcs.mbraw.controller;

import lombok.Data;

@Data
public class YoutubeDLForm {
	private String url;
	private String videoId;
	private boolean toMp3 ;
}
