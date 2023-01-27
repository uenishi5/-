import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.system.SystemProperties;

public class TestProgram {

	public static void main(String[] args) throws URISyntaxException {

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
			// -x : (–extract-audio) で音声のみダウンロード
			// –audio-format mp3: mp3 形式にフォーマット
			builder.command("youtube-dl", "-v", "https://www.youtube.com/watch?v=nOEWheGx-ig");
			builder.command().forEach(System.out::println);

			// youtube-dl https://www.youtube.com/watch?v=nOEWheGx-ig

			// コマンドを実行する
			Process process = builder.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
