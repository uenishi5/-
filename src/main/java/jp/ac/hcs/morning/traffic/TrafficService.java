package jp.ac.hcs.morning.traffic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class TrafficService {
	/** 高速バス情報 */
	private static final String NORTH_SAPPORO_ISHIKARI = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=16";
	private static final String SOUTH_SAPPORO_NORTH_HIROSHIMA_CHITOSE_EBETSU = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=17";
	private static final String OTARU_SHINAI = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=12";
	private static final String OTARU_HOUMEN = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=13";
	private static final String TAKIGAWA = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=11";
	private static final String IWAMIZAWA = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=10";
	private static final List<String> BUSLIST = Arrays.asList(NORTH_SAPPORO_ISHIKARI, SOUTH_SAPPORO_NORTH_HIROSHIMA_CHITOSE_EBETSU, OTARU_SHINAI, OTARU_HOUMEN, TAKIGAWA, IWAMIZAWA);

	private static final String NONE = "現在、運休の情報はありません。 道路状況等により遅延が発生する場合がありますので、 詳しくは各ターミナル・営業所にお問合せください。";

	/** 指定された交通情報を取得するメソッド */
	public TrafficEntity getBusdata(int no) {

		// 通信が正常にできるか
		Document document = null;

		try {
			document = Jsoup.connect(BUSLIST.get(no)).get();
		}
		catch (IOException e) {
			return TrafficEntity.error();
		}

		final TrafficEntity entity = new TrafficEntity();
		final TrafficData flg = new TrafficData();

		flg.setAlertflg(false);

		Elements td = document.getElementsByTag("td");
		Elements rosenlist = document.select(".Rsn");

		if (document.select(".F16").text().equals(NONE)) {
			return TrafficEntity.error();
		}

		List<String> tdlist = td.stream().map(td1 -> td1.text()).toList();

		for (Element rosen : rosenlist) {
			final String rowspansub = rosen.attr("rowspan");
			final int rowspan = rowspansub.equals("") ? 1 : Integer.parseInt(rowspansub);

			for (int count = 0; count < rowspan; count++) {
				final TrafficData rosenData = new TrafficData();

				rosenData.setRosen((count == 0) ? rosen.text() : null);
				rosenData.setHoumen(tdlist.get(0));
				rosenData.setSituation(tdlist.get(1));
				rosenData.setRemarks(tdlist.get(2));
				rosenData.setCount(count);
				rosenData.setRowspan(rowspan);

				entity.getTrafficrosenList().add(rosenData);
			}
		}

		entity.getTrafficflgList().add(flg);
		return entity;
	}

}