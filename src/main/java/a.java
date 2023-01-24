import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class a {

	public static void main(String[] args) throws URISyntaxException {
		ProcessBuilder builder = new ProcessBuilder();

		try {

			Path directory = Paths.get("").toAbsolutePath();
			System.out.println(directory);

			builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			builder.redirectError(ProcessBuilder.Redirect.INHERIT);
			builder.directory(directory.toFile());

			builder.command("youtube-dl.exe", "https://www.youtube.com/watch?v=nOEWheGx-ig");

			Process process = builder.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
