package jp.ac.hcs.mbraw.controller.chart;

import java.util.Objects;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import jp.ac.hcs.mbraw.HttpConnectUtils;

/**
 * Bitcoinのチャート情報を取得するサービス
 *
 */
@Service
public class ChartService {

	private static final String URL = "https://bitflyer.com/ja-jp/bitcoin-chart";

	public ChartEntity getChartData() {
		final Document document = HttpConnectUtils.getDocument(URL);

		if (Objects.isNull(document)) {
			// 通信に失敗した場合は、ChartEntityクラスのエラー専用のオブジェクトを返す
			return ChartEntity.error();
		}

		// 通信成功したのでデータを設定する
		final ChartEntity entity = new ChartEntity();
		final ChartData data = new ChartData();

		/** 現在のビットコインの価値と、前回との差を取得する */
		final Elements bitcoin_rate = document.select(".p-currencyInfo__head");

		/** 過去２４時間の最高値と最低値、時価総額を取得する */
		final Elements max_minrate = document.select(".p-currencyInfo__info");

		/** 現在のビットコインの価値と、前回との差を格納する */
		final String[] bitcoin_ratelist = bitcoin_rate.text().split(" ");
		final String bitcoin = bitcoin_ratelist[0].replace("※", " ");
		final String rate = bitcoin_ratelist[1];

		/** 過去２４時間の最高値と最低値、時価総額を格納する */
		final String[] max_minratelist = max_minrate.text().split(" ");
		final String maxrate = max_minratelist[3];
		final String minrate = max_minratelist[5];
		final String market_capitalization = max_minratelist[7];

		data.setBitcoin(bitcoin);
		data.setRate(rate);
		data.setMaxrate(maxrate);
		data.setMinrate(minrate);
		data.setMarket_capitalization(market_capitalization);
		entity.getChartList().add(data);
		return entity;
	}
}
