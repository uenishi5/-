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
	/** 高速バス一覧ページ */
	private static final String TOP_BUSPAGE = "https://www.chuo-bus.co.jp/support/stop/";
	/** 高速道路交通情報一覧ページ */
	private static final String TOP_HIGHWAY = "https://roadway.yahoo.co.jp/traffic/pref/1/list";
	/** 高速バス情報 */
	private static final String NORTH_SAPPORO_ISHIKARI = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=16";
	private static final String SOUTH_SAPPORO_NORTH_HIROSHIMA_CHITOSE_EBETSU = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=17";
	private static final String OTARU_SHINAI = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=12";
	private static final String OTARU_HOUMEN = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=13";
	private static final String TAKIGAWA = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=11";
	private static final String IWAMIZAWA = "https://www.chuo-bus.co.jp/support/stop/?ope=list&g=10";
	private static final String BUSLIST[] = { NORTH_SAPPORO_ISHIKARI, SOUTH_SAPPORO_NORTH_HIROSHIMA_CHITOSE_EBETSU,
			OTARU_SHINAI, OTARU_HOUMEN, TAKIGAWA, IWAMIZAWA };
	/** 高速道路交通情報*/
	private static final String DOUOU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/1001001/list";
	private static final String SASSON = "https://roadway.yahoo.co.jp/traffic/pref/1/road/1003001/list";
	private static final String SIRIBESHI = "https://roadway.yahoo.co.jp/traffic/pref/1/road/1003101/list";
	private static final String DOUTOU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/1005001/list";
	private static final String FUKAGAWA_RUMOI = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3100070/list";
	private static final String HIDAKA = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3100090/list";
	private static final String HAKODATE_ESASHI = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3503010/list";
	private static final String ASAHIKAWA_MONBETSU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3503040/list";
	private static final String OBIHIRO_HIROO = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3503050/list";
	private static final String HAKODATE_SHINDOU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3506010/list";
	private static final String KUROMATSUNAI = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3503010/list";
	private static final String NAYORO_BIFUKA = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3506030/list";
	private static final String BIHORO_BAIPASU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3506040/list";
	private static final String HOROTOMI_BAIPASU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3506050/list";
	private static final String TOYOTOMI_BAIPASU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3506060/list";
	private static final String KUSHIRO_GAIKANJOU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3506080/list";
	private static final String NEMURO = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3506090/list";
	private static final String TOKACHI_OHOTUKU = "https://roadway.yahoo.co.jp/traffic/pref/1/road/3509990/list";
	private static final String HIGHWAYLIST[] = { DOUOU, SASSON, SIRIBESHI, DOUTOU, FUKAGAWA_RUMOI, HIDAKA,
			HAKODATE_ESASHI, ASAHIKAWA_MONBETSU, OBIHIRO_HIROO, HAKODATE_SHINDOU, KUROMATSUNAI, NAYORO_BIFUKA,
			BIHORO_BAIPASU, HOROTOMI_BAIPASU, TOYOTOMI_BAIPASU, KUSHIRO_GAIKANJOU, NEMURO, TOKACHI_OHOTUKU };


/** 指定された交通情報を取得するメソッド*/
	public TrafficEntity getBusdata(int no) {
		TrafficEntity entity = new TrafficEntity();
		TrafficData flg = new TrafficData();
		flg.setAlertflg(false);

		Document document;
		try {

			document = Jsoup.connect(BUSLIST[no]).get();
			Elements f16 = document.select(".F16");
			Elements td = document.getElementsByTag("td");
			Elements rosenlist = document.select(".Rsn");
			int rosencount = 0;
			int idx = 0;
			if (!(f16.text().equals("現在、運休の情報はありません。 道路状況等により遅延が発生する場合がありますので、 詳しくは各ターミナル・営業所にお問合せください。"))) {
				ArrayList<String> tdlist = new ArrayList<>();
				for (Element td1 : td) {
					tdlist.add(td1.text());
					idx++;
				}
				idx = 0;
				for (Element rosen : rosenlist) {
					int rowspan = Integer.parseInt(rosen.attr("rowspan"));
					boolean roopflg = false;
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