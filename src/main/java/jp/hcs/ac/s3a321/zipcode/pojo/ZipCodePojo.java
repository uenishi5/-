
package jp.hcs.ac.s3a321.zipcode.pojo;

import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"message",
		"results",
		"status"
})
@Generated("jsonschema2pojo")
public class ZipCodePojo {

	@JsonProperty("message")
	public Object message;
	@JsonProperty("results")
	public List<Result> results = null;
	@JsonProperty("status")
	public int status;

	public static ZipCodePojo empty() {
		return new ZipCodePojo();
	}

}
