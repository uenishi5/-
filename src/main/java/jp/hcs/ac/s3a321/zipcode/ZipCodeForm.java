package jp.hcs.ac.s3a321.zipcode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * 検索項目を保持するクラス
 * @author s20203029
 */
@Data
public class ZipCodeForm {
	@NotEmpty
	@Pattern(regexp = "^[0-9]{7}$")
	private String zipCode;
}
