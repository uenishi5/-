package jp.ac.hcs.morning.chart;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
/**
 * Bitcoinのチャート情報を取得するサービス
 *
 */
@Service
public class ChartService {
	public ChartEntity getChartData() {
		ChartEntity entity = new ChartEntity();
		ChartData data = new ChartData();
		Document document;
		try {
			document = Jsoup.connect("https://bitflyer.com/ja-jp/bitcoin-chart").get();
			/** 現在のビットコインの価値と、前回との差を取得する*/
			Elements bitcoin_rate = document.select(".p-currencyInfo__head");
			/** 過去２４時間の最高値と最低値、時価総額を取得する*/
			Elements max_minrate = document.select(".p-currencyInfo__info");
			/** 現在のビットコインの価値と、前回との差を格納する*/
			String[] bitcoin_ratelist = null;
			bitcoin_ratelist = bitcoin_rate.text().split(" ");
			String bitcoin = bitcoin_ratelist[0].replace("※", " ");
			String rate = bitcoin_ratelist[1];
			data.setBitcoin(bitcoin);
			data.setRate(rate);
			/** 過去２４時間の最高値と最低値、時価総額を格納する*/
			String[] max_minratelist = null;
			max_minratelist = max_minrate.text().split(" ");
			String maxrate = max_minratelist[3];
			String minrate = max_minratelist[5];
			String market_capitalization = max_minratelist[7];
			data.setMaxrate(maxrate);
			data.setMinrate(minrate);
			data.setMarket_capitalization(market_capitalization);
			entity.getChartList().add(data);
		} catch (IOException e) {
			data.setMaxrate("取得できませんでした");
			data.setMinrate("取得できませんでした");
			data.setMarket_capitalization("取得出来ませんでした");
			entity.getChartList().add(data);
		}
		return entity;
	}
}
