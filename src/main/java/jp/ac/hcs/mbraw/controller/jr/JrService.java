package jp.ac.hcs.mbraw.controller.jr;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import jp.ac.hcs.mbraw.HttpConnectUtils;

@Service
public class JrService {
	// リンクの宣言
	private static final String TOUZAI = "https://transit.yahoo.co.jp/diainfo/13/0";
	private static final String NANBOKU = "https://transit.yahoo.co.jp/diainfo/14/0";
	private static final String TOUHOU = "https://transit.yahoo.co.jp/diainfo/15/0";
	private static final String SHIDEN = "https://transit.yahoo.co.jp/diainfo/545/0";
	private static final List<String> JRLIST = Arrays.asList(TOUZAI, NANBOKU, TOUHOU, SHIDEN);

	// JR交通情報の取得を行うメソッド
	public JrEntity getallJrData() {

		final JrEntity entity = new JrEntity();

		final List<JrData> jrList = JRLIST.stream().map(url -> {
			final Document document = HttpConnectUtils.getDocument(url);

			if (Objects.isNull(document)) {
				// 例外が発生した場合は、エラー専用のオブジェクトを返す
				return JrData.error();
			}

			final JrData data = new JrData();
			final String[] title = document.select(".title").text().split(" ");
			final String alertname = document.select("dt").text().substring(3, 7);
			final boolean alertFlag = document.select(".normal").text() == null;

			data.setTitle(title[0]);
			data.setAlertname(alertname);
			data.setAlert(alertFlag);

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
