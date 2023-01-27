package jp.ac.hcs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 設定したAPIキーを保持するクラス
 */

@Data
@Component
@ConfigurationProperties(prefix = "apikey")
public class ApiKeyHolder {

	private String news;

	private String youtube;
}
