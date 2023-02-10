package jp.ac.hcs.mbraw.controller;

import com.ren130302.webapi.pornhubapi.request.Search;
import com.ren130302.webapi.pornhubapi.request.Search.Builder;

import lombok.Data;

@Data
public class PornhubForm {
	private String q;

	public Search.Builder query(Builder b) {
		b.q(this.getQ());
		return b;
	}
}
