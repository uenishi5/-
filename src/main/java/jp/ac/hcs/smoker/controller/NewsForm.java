package jp.ac.hcs.smoker.controller;

import com.ren130302.webapi.newsapi.enums.NewsLanguage;
import com.ren130302.webapi.newsapi.enums.NewsSearchIn;
import com.ren130302.webapi.newsapi.enums.NewsSort;
import com.ren130302.webapi.newsapi.request.Everything;

import lombok.Data;

@Data
public class NewsForm {
	private String q;
	private NewsSort sort = NewsSort.PUBLISHED_AT;
	private NewsLanguage language = NewsLanguage.EN;
	private NewsSearchIn[] searchIns = NewsSearchIn.values();

	public Everything.Builder query(Everything.Builder b) {
		return b.sortBy(this.getSort()).languages(this.getLanguage()).q(this.getQ()).searchIn(this.getSearchIns());
	}
}