
package jp.ac.hcs.mbraw.controller.horoscope.pojo;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"content",
		"item",
		"money",
		"total",
		"job",
		"color",
		"day",
		"love",
		"rank",
		"sign"
})
@Generated("jsonschema2pojo")
@Data
public class HoroscopeInfo {

	@JsonProperty("content")
	public String content;
	@JsonProperty("item")
	public String item;
	@JsonProperty("money")
	public int money;
	@JsonProperty("total")
	public int total;
	@JsonProperty("job")
	public int job;
	@JsonProperty("color")
	public String color;
	@JsonProperty("day")
	public String day;
	@JsonProperty("love")
	public int love;
	@JsonProperty("rank")
	public int rank;
	@JsonProperty("sign")
	public String sign;

}
