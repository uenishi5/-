package jp.ac.hcs.smoker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "newsapi")
public class Holder {

	private String apiKey;
}
