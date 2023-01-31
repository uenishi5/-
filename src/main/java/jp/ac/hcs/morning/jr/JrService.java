package jp.ac.hcs.morning.jr;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
				Elements normal = document.select(".normal");
				Elements trouble_suspend = document.select(".trouble suspend");
				Elements trouble = document.select(".trouble");
				Elements dt = document.select("dt");
				Elements title = document.select(".title");
				String[] title2 = title.text().split(" ");
				data.setTitle(title2[0]);
				String[] alert = dt.text().split(" ");
				String alertname = alert[0].substring(3);
				data.setAlertname(alertname);
				while (true) {
					if (normal.text() != null) {
						data.setContent(normal.text());
						data.setColor("green");
						break;
					} else if (trouble_suspend.text() != null) {
						data.setColor("purple");
						data.setContent(trouble_suspend.text());
						break;
					} else if (trouble.text() != null) {
						data.setColor("red");
						data.setContent(trouble.text());
						break;
					}
					break;
				}
				entity.getJrList().add(data);
			}

		} catch (IOException e) {

		}

		return entity;
	}


}
