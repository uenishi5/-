import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.system.SystemProperties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

public class TestProgram {

	public static void download(String url, boolean toMp3) {
		try {
			// コマンド実行のためのビルダー
			final ProcessBuilder builder = new ProcessBuilder();

			// コマンドプロンプトの内容をコンソールに出力する設定
			builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			builder.redirectError(ProcessBuilder.Redirect.INHERIT);

			// 使用しているOSを調べる
			final String osName = SystemProperties.get("os.name");
			final boolean isWindows = osName.startsWith("Windows");
			System.out.println(String.format("OS name=%S, isWindows=%B", osName, isWindows));

			if (isWindows) {
				// 現在のプロジェクト相対パスから絶対パスに変換
				final Path absolutePath = Paths.get("").toAbsolutePath();
				// ディレクトリの設定
				builder.directory(absolutePath.toFile());
			}

			System.out.println(String.format("Directory=%S", builder.directory()));

			// 実行するコマンドの設定
			// -v : debug mode
			// -x : (--extract-audio) で音声のみダウンロード
			// --audio-format mp3: mp3 形式にフォーマット
			List<String> command = new ArrayList<>();
			command.add("youtube-dl");
			command.add("-v");
			command.add(url);

			if (toMp3) {
				System.out.println("Extract Audio");
				command.add("--extract-audio");
				command.add("--audio-format");
				command.add("mp3");
			}

			builder.command(command);
			builder.command().forEach(System.out::println);

			// コマンドを実行する
			Process process = builder.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static TestA jsonToTestAClass(String json) {
		// 文字列 から クラス に変換するプログラム

		// インスタンス生成
		final ObjectMapper objectMapper = new ObjectMapper();

		try {
			// "json形式の文字列" から "TestClassA クラス" に変換
			// 変換することによってデータをオブジェクトとして扱うことができる
			// 必ず JsonMappingexception と JsonProcessingException をキャッチする
			return objectMapper.readValue(json, TestA.class);
		}
		catch (JsonMappingException e) {
			// データのエンコード/デコードに問題があった際に発生する（直訳）
			e.printStackTrace();
		}
		catch (JsonProcessingException e) {
			// JSON コンテンツを処理（パース、生成）する際に発生する（直訳）
			e.printStackTrace();
		}

		return new TestA();
	}

	public static String testAClassToJson() {
		// クラス から 文字列 に変換するプログラム

		// クラスの初期化
		TestA testA = new TestA();
		testA.setId(2);
		testA.setLabel("testLabelTest");

		// インスタンス生成
		final ObjectMapper objectMapper = new ObjectMapper();

		try {
			// "TestClassA クラス" から "json形式の文字列"に変換
			// 必ず JsonProcessingException をキャッチする
			// writerWithDefaultPrettyPrinterメソッドは人が見やすいようにフォーマットする設定
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testA);
		}
		catch (JsonProcessingException e) {
			// JSON コンテンツを処理（パース、生成）する際に発生する（直訳）
			e.printStackTrace();
		}

		return "";
	}

	@Data
	public static class TestA {
		private String label;
		private int id;
	}

	public static void main(String[] args) {
		download("", false);
	}
}
