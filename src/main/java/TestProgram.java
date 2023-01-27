import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.system.SystemProperties;

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

			builder.command("youtube-dl", "-v", url);
			builder.command().forEach(System.out::println);

			// コマンドを実行する
			Process process = builder.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		download("", false);
	}
}
