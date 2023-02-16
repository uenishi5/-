package jp.ac.hcs.mbraw;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.system.SystemProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriUtils;

import com.google.common.hash.Hashing;

public class DownloadHelper {

	private static final Map<String, Byte> REQUEST = new HashMap<>();

	private static final byte ERROR = -1;
	private static final byte NONE = 0;
	private static final byte OK = 1;

	private static final String CONTENT_DISPOSITION_FORMAT = "attachment; filename=\"%s\"; filename*=UTF-8''%s";

	public static void addContentDisposition(HttpHeaders headers, File file) {
		headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT, file.getName(), UriUtils.encode(file.getName(), StandardCharsets.UTF_8.name())));
	}

	public static ResponseEntity<byte[]> sendToClient(File file) {
		// サーバーからクライアントにダウンロードをさせる
		// 設定中に例外が発生した場合は、問題なしと見なし 200コード を返す
		try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
			return ResponseEntity.ok().headers(header -> addContentDisposition(header, file)).contentType(MediaType.APPLICATION_OCTET_STREAM).body(IOUtils.toByteArray(inputStream));
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(e.getMessage().getBytes());
		}
	}

	public static Process download(String url, boolean toMp3) throws IOException {
		// コマンド実行のためのビルダー
		final ProcessBuilder builder = new ProcessBuilder();
		final List<String> command = new ArrayList<>();

		// コマンドプロンプトの内容をコンソールに出力する設定
		builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		builder.redirectError(ProcessBuilder.Redirect.INHERIT);

		// 使用しているOSを調べる
		final String osName = SystemProperties.get("os.name");
		final boolean isWindows = osName.startsWith("Windows");
		final boolean isMacOS = osName.startsWith("MacOS");
		// 使用するOSによって実行するコマンドファイルを変更する
		final String executeFile = isWindows ? "yt-dlp.exe"
				: isMacOS ? "yt-dlp_macos"
				: "yt-dlp";

		// 実行するコマンドの設定
		command.add(Paths.get("cmds", executeFile).toString());
		// -v : debug mode
		// -x : (--extract-audio) で音声のみダウンロード
		// --audio-format mp3: mp3 形式にフォーマット
		// ダウンロードしたい動画の url を設定
		command.add(url);
		// 保存場所を ユーザのダウンロードへ設定
		// ファイル名をidにする
		command.add("--output");
		command.add("~/downloads/%(id)s.%(ext)s");

		// mp3に変換するコマンド
		if (toMp3) {
			// mp3 を希望したときのみコマンド引数を追加
			System.out.println("Extract Audio");
			command.add("--format");
			command.add("worstvideo+bestaudio");
			// 音声ファイルの抽出おおび形式を設定
			command.add("--extract-audio");
			command.add("--audio-format");
			command.add("mp3");
		}

		// 一連のコマンドをプロセスビルダーに設定をして
		// 実行するコマンドを出力
		builder.command(command);

		// コマンドを実行する
		return builder.start();
	}

	public static ResponseEntity<byte[]> execute(Path path, String url, boolean toMp3) {

		final String sha256hex = Hashing.sha256().hashString(url, StandardCharsets.UTF_8).toString();
		REQUEST.put(sha256hex, NONE);

		if (Files.exists(path)) {
			return sendToClient(path.toFile());
		}

		Process process = null;

		// 指定されたURLの動画をダウンロードするコマンドを実行
		try {
			process = download(url, toMp3);
		}
		catch (IOException e) {
			e.printStackTrace();
			REQUEST.put(sha256hex, ERROR);
		}

		// ダウンロード完了するまで待機する
		// 状態がOKになるまで待機する
		// 状態がエラーになった場合はプロセスを殺す
		long start = System.currentTimeMillis();

		while (REQUEST.get(sha256hex) != OK) {
			if (Files.exists(path)) {
				REQUEST.put(sha256hex, OK);
			}
			else if (REQUEST.get(sha256hex) == ERROR) {
				if (Objects.nonNull(process)) {
					process.destroy();
				}
				REQUEST.remove(sha256hex);
				return ResponseEntity.ok().build();
			}

			if (System.currentTimeMillis() - start >= 1000L) {
				start = System.currentTimeMillis();
				System.out.println("Wait for download to complete");
			}
		}

		REQUEST.remove(sha256hex);

		try {
			// 0byteでダウンロード開始されるため
			// １秒待機させてから次の仕事をさせる
			Thread.sleep(1000L);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Download complete");
		return sendToClient(path.toFile());
	}

	public static void main(String[] args) throws IOException {
		download("https://jp.pornhub.com/view_video.php?viewkey=ph615a85ea5ca11", false);

		String absPath = Paths.get("cmds").toAbsolutePath().toString();
		System.out.println(absPath);
	}
}
