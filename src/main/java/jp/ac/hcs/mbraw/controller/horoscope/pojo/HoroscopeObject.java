
package jp.ac.hcs.mbraw.controller.horoscope.pojo;

import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"horoscope"
})
@Generated("jsonschema2pojo")
@Data
public class HoroscopeObject {

	@JsonProperty("horoscope")
	public Map<String, List<HoroscopeInfo>> horoscope;

}
