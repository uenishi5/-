package jp.ac.hcs.mbraw.controller.chart;

import java.util.List;
import java.util.Objects;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import jp.ac.hcs.mbraw.HttpConnectUtils;
import jp.ac.hcs.mbraw.controller.AttributeEntity;

/**
 * Bitcoinのチャート情報を取得するサービス
 *
 */
@Service
public class ChartService {

	private static final String CHART_ENTITY = "ChartEntity";
	
	private static final AttributeEntity<ChartData> ERROR = AttributeEntity.set(ChartService.CHART_ENTITY, ChartData.error());
	 
	private static final String URL = "https://bitflyer.com/ja-jp/bitcoin-chart";
	
	
	public AttributeEntity<ChartData> getChartData() {
		final Document document = HttpConnectUtils.getDocument(ChartService.URL);

		if (Objects.isNull(document)) {
			// 通信に失敗した場合は、ChartEntityクラスのエラー専用のオブジェクトを返す
			return ERROR;
		}

		// 通信成功したのでデータを設定する
		final ChartData data = ChartData.empty();

		/** 現在のビットコインの価値と、前回との差を取得する */
		// css(. p-currencyInfo__head) を指定されているタグを取得
		final Elements elementHeadRate = document.select(".p-currencyInfo__head");

		/** 過去２４時間の最高値と最低値、時価総額を取得する */
		final Elements elementInfoRate = document.select(".p-currencyInfo__info");

		/** 現在のビットコインの価値と、前回との差を格納する */
		final String bitcoin = elementHeadRate.select(".p-currencyInfo__price.c-text--number").eachText().get(0)
				.replace("※", "");
		final String rate = elementHeadRate.select(".c-rate.up.p-currencyInfo__rate").eachText().get(0);

		/** 過去２４時間の最高値と最低値、時価総額を格納する */
		final List<String> infoRateList = elementInfoRate.select(".p-currencyInfo__table__td").eachText();
		final String maxRate = infoRateList.get(0);
		final String minRate = infoRateList.get(1);
		final String marketCapitalization = infoRateList.get(2);

		data.setBitcoin(bitcoin);
		data.setRate(rate);
		data.setMaxRate(maxRate);
		data.setMinRate(minRate);
		data.setMarketCapitalization(marketCapitalization);

		return AttributeEntity.set(ChartService.CHART_ENTITY, data);
	}
}
