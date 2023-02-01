package jp.ac.hcs.morning.jr;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class JrService {
	//リンクの宣言
	private static final String TOUZAI = "https://transit.yahoo.co.jp/diainfo/13/0";
	private static final String NANBOKU = "https://transit.yahoo.co.jp/diainfo/14/0";
	private static final String TOUHOU = "https://transit.yahoo.co.jp/diainfo/15/0";
	private static final String SHIDEN = "https://transit.yahoo.co.jp/diainfo/545/0";
	private static final String JRLIST[] = { TOUZAI, NANBOKU, TOUHOU, SHIDEN };

	//JR交通情報の取得を行うメソッド
	public JrEntity getallJrData() {
		Document document;
		JrEntity entity = new JrEntity();
		try {

			for (String list : JRLIST) {
				JrData data = new JrData();
				document = Jsoup.connect(list).get();
				String[] title = document.select(".title").text().split(" ");
				String alertname = document.select("dt").text().substring(3,7);
				data.setTitle(title[0]);
				data.setAlertname(alertname);

				if ( document.select(".normal").text() != null) {
					data.setContent(document.select(".normal").text());
					data.setColor("green");

				} else if (document.select(".trouble suspend").text() != null) {
					data.setColor("purple");
					data.setContent(document.select(".trouble suspend").text());

				} else if (document.select(".trouble").text() != null) {
					data.setColor("red");
					data.setContent(document.select(".trouble").text());

				}
				entity.getJrList().add(data);
			}
		} catch (IOException e) {
			JrData data = new JrData();
			data.setContent("エラー");
			data.setColor("red");
			
		}

		return entity;
	}

}
