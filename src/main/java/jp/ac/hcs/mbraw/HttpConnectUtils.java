package jp.ac.hcs.mbraw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectUtils {

	/**
	 * 接続した後にapiを取得する
	 *
	 * @param url
	 * @return 接続に失敗した場合は空の文字列を返す。成功にしている場合は中身を返す
	 */
	public static String connectAndGetText(String url) {

		HttpURLConnection connection = null;

		// URLが正常なものか確認するための try-catch 文
		// APIへリクエスト送信
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.connect();
		}
		catch (IOException e) {
			// URL解析した結果エラーが発生したまたは接続失敗した場合、エラー専用のHoroscopeEntityオブジェクトを返す
			e.printStackTrace();
			return "";
		}

		final StringBuffer buf = new StringBuffer();
		String tmp = "";
		try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			while ((tmp = in.readLine()) != null) {
				buf.append(tmp);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			// データ取得最中に異常が発生した場合、エラー専用のHoroscopeEntityオブジェクトを返す
			return "";
		}

		connection.disconnect();

		return buf.toString();
	}
}
