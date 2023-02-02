package jp.ac.hcs.morning.traffic;

import java.io.IOException;
import java.util.ArrayList;

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
	private static final String BUSLIST[] = { NORTH_SAPPORO_ISHIKARI, SOUTH_SAPPORO_NORTH_HIROSHIMA_CHITOSE_EBETSU,
			OTARU_SHINAI, OTARU_HOUMEN, TAKIGAWA, IWAMIZAWA };


/** 指定された交通情報を取得するメソッド*/
	public TrafficEntity getBusdata(int no) {
		TrafficEntity entity = new TrafficEntity();
		TrafficData flg = new TrafficData();
		flg.setAlertflg(false);

		Document document;
		try {

			document = Jsoup.connect(BUSLIST[no]).get();
			Elements td = document.getElementsByTag("td");
			Elements rosenlist = document.select(".Rsn");
			int rosencount = 0;
			int idx = 0;
			int rowspan = 1;
			if (!(document.select(".F16").text().equals("現在、運休の情報はありません。 道路状況等により遅延が発生する場合がありますので、 詳しくは各ターミナル・営業所にお問合せください。"))) {
				ArrayList<String> tdlist = new ArrayList<>();
				for (Element td1 : td) {
					tdlist.add(td1.text());
					idx++;
				}
				idx = 0;
				for (Element rosen : rosenlist) {
					String rowspansub = rosen.attr("rowspan");
					if(rowspansub.equals("")) {
						rowspan = 1;
					}else{
						rowspan = Integer.parseInt(rowspansub);
					}
					for (int count = 0; count < rowspan; count++) {
						TrafficData rosendata = new TrafficData();
						if(!(count == 0)) {
							rosendata.setRosen(null);
						}else {
						rosendata.setRosen(rosen.text());
						}
						rosendata.setHoumen(tdlist.get(idx));
						idx++;
						rosendata.setSituation(tdlist.get(idx));
						idx++;
						rosendata.setRemarks(tdlist.get(idx));
						idx++;
						rosendata.setCount(rosencount);
						rosendata.setRowspan(rowspan);
						rosencount++;

						entity.getTrafficrosenList().add(rosendata);
					}
				}
			} else {
				flg.setAlertflg(true);
			}
		} catch (IOException e) {
			flg.setAlertflg(true);
		}
		entity.getTrafficflgList().add(flg);
		return entity;
	}

}