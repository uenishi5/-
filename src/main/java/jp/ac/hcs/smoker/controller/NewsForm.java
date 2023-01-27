package jp.ac.hcs.smoker.controller;

import com.ren130302.webapi.newsapi.enums.NewsLanguage;
import com.ren130302.webapi.newsapi.enums.NewsSort;
import com.ren130302.webapi.newsapi.request.Everything;

import lombok.Data;

@Data
public class NewsForm {
	private String q;
	private NewsSort sort = NewsSort.PUBLISHED_AT;

	public Everything.Builder query(Everything.Builder b) {
		b.sortBy(this.sort).languages(NewsLanguage.EN).q(this.getQ());
		return b;
	}
}