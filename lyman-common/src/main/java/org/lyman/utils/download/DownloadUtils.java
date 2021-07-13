package org.lyman.utils.download;

import org.lyman.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DownloadUtils {

    public static void download(HttpServletResponse response, File file, String filename) throws FileNotFoundException {
        if (StringUtils.isEmpty(filename))
            filename = file.getName();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        download(response, os -> {
            final int bufferLen = 8192;
            byte[] buffer = new byte[bufferLen];
            int len, i = 0;
            while ((len = bis.read(buffer)) > 0)
                os.write(buffer, i++ * bufferLen, len);
        }, filename);
    }

    public static void download(HttpServletResponse response, OutputStreamHandler handler, String filename) {

    }

    public interface OutputStreamHandler {
        void handle(OutputStream os) throws IOException;
    }

}
