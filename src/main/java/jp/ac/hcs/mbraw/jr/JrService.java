package jp.ac.hcs.mbraw.jr;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class JrService {
	// リンクの宣言
	private static final String TOUZAI = "https://transit.yahoo.co.jp/diainfo/13/0";
	private static final String NANBOKU = "https://transit.yahoo.co.jp/diainfo/14/0";
	private static final String TOUHOU = "https://transit.yahoo.co.jp/diainfo/15/0";
	private static final String SHIDEN = "https://transit.yahoo.co.jp/diainfo/545/0";
	private static final List<String> JRLIST = Arrays.asList(TOUZAI, NANBOKU, TOUHOU, SHIDEN);


	/** メイン画面用Jr情報取得 */
	public boolean getMainJrFlg() {
		boolean flg = false;
		try {
			Document touzai = Jsoup.connect(TOUZAI).get();
			Document nanboku = Jsoup.connect(NANBOKU).get();
			Document touhou = Jsoup.connect(TOUHOU).get();
			Document shiden = Jsoup.connect(SHIDEN).get();
			if (touzai.select(".normal").text() == null || nanboku.select(".normal").text() == null
					|| touhou.select(".normal").text() == null || shiden.select(".normal").text() == null) {
				flg = true;
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return flg;
	}
	
	// JR交通情報の取得を行うメソッド
	public JrEntity getallJrData() {

		final JrEntity entity = new JrEntity();

		final List<JrData> jrList = JRLIST.stream().map(url -> {
			Document document = null;

			try {
				document = Jsoup.connect(url).get();
			}
			catch (IOException e) {
				// 例外が発生した場合は、エラー専用のオブジェクトを返す
				e.printStackTrace();
				return JrData.error();
			}

			final JrData data = new JrData();
			final String[] title = document.select(".title").text().split(" ");
			final String alertname = document.select("dt").text().substring(3, 7);

			data.setTitle(title[0]);
			data.setAlertname(alertname);

			if (document.select(".normal").text() != null) {
				data.setColor("green");
				data.setContent(document.select(".normal").text());
			}
			else if (document.select(".trouble suspend").text() != null) {
				data.setColor("purple");
				data.setContent(document.select(".trouble suspend").text());

			}
			else if (document.select(".trouble").text() != null) {
				data.setColor("red");
				data.setContent(document.select(".trouble").text());
			}

			return data;
		}).toList();

		entity.getJrList().addAll(jrList);

		return entity;
	}

}
