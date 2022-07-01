package jp.hcs.ac.s3a321.main;

import lombok.Data;

@Data
public class MessageEntity {
	/** エラーメッセージ(表示用) */
	private String errorMessage = "";

	/** メッセージ(表示用)*/
	private String message = "";
}
