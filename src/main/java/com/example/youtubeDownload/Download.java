package com.example.youtubeDownload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Download {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://www.youtube.com/watch?v=D_MXBWpoygE"); // ダウンロードする URL
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();

        File file = new File("/Users/sawadayuunari/Desktop/youtubeDownload/src/main/resources/static/movie"); // 保存先
        FileOutputStream out = new FileOutputStream(file, false);
        byte[] bytes = new byte[512];
        while (true) {
            int ret = in.read(bytes);
            if (ret == 0) break;
            out.write(bytes, 0, ret);
        }

        out.close();
        in.close();
    }
}
