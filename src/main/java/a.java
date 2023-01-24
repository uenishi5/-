import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.system.SystemProperties;

public class a {

	public static void main(String[] args) throws URISyntaxException {

		try {
			// コマンド実行のためのビルダー
			final ProcessBuilder builder = new ProcessBuilder();

			// コマンドプロンプトの内容をコンソールに出力する設定
			builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			builder.redirectError(ProcessBuilder.Redirect.INHERIT);

			// 現在のプロジェクト相対パスから絶対パスに変換
			final Path absolutePath = Paths.get("").toAbsolutePath();
			System.out.println(absolutePath);
			// ディレクトリの設定
			builder.directory(absolutePath.toFile());

			// 使用しているOSを調べる
			final String osName = SystemProperties.get("os.name");
			final boolean isWindows = osName.startsWith("Windows");
			final String exeFile = isWindows ? "youtube-dl.exe" : "youtube-dl";

			System.out.println(String.format("OS name=%S, isWindows=%B", osName, isWindows));

			// 実行するコマンドの設定
			builder.command(exeFile, "https://www.youtube.com/watch?v=nOEWheGx-ig");

			// コマンドを実行する
			Process process = builder.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
