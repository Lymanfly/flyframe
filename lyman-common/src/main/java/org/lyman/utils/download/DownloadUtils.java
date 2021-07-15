package org.lyman.utils.download;

import org.lyman.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class DownloadUtils {

    public static void download(HttpServletResponse response, File file, String filename, boolean isAttachment)
            throws IOException {
        if (StringUtils.isEmpty(filename))
            filename = file.getName();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        download(response, os -> {
            final int bufferLen = 8192;
            byte[] buffer = new byte[bufferLen];
            int len;
            while ((len = bis.read(buffer)) > 0)
                os.write(buffer, 0, len);
        }, filename, file.length(), isAttachment);
    }

    public static void download(HttpServletResponse response, OutputStreamHandler handler,
                                String filename, boolean isAttachment) throws IOException {
        download(response, handler, filename, -1L, isAttachment);
    }

    /**
     * 2010年<a href=https://datatracker.ietf.org/doc/html/rfc5987> RFC 5987 <a/>发布，
     * 正式规定了 HTTP Header 中多语言编码的处理方式采用 parameter*=charset'lang'value 的格式，
     * 其中：<br/>
     *
     * 1. charset 和 lang 不区分大小写<br/>
     * 2. lang 是用来标注字段的语言，以供读屏软件朗诵或根据语言特性进行特殊渲染，可以留空<br/>
     * 3. value 根据 RFC 3986 Section 2.1 使用百分号编码，并且规定浏览器至少应该支持 ASCII 和 UTF-8<br/>
     * 4. 当 parameter 和 parameter* 同时出现在 HTTP 头中时，浏览器应当使用后者<br/>
     *
     * @param response
     * @param handler
     * @param filename
     * @param contentLength
     * @param isAttachment
     * @throws IOException
     */
    public static void download(HttpServletResponse response, OutputStreamHandler handler,
                                String filename, long contentLength, boolean isAttachment) throws IOException {
        checkArgs(response, handler, filename);
        response.reset();
        String[] arr = filename.split("\\.");
        String ext = arr[arr.length - 1];
        String filenameEncoded = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        String cd = (isAttachment ? "attachment; " : "").concat("filename=\"")
                .concat(filenameEncoded).concat("\"; filename*=utf-8''").concat(filenameEncoded);
        response.setContentType(ContentType.getContentType(ext));
        response.setHeader("Content-Disposition", cd);
        if (contentLength > 0)
            response.setContentLengthLong(contentLength);
        else if (!response.containsHeader("Transfer-Encoding"))
            response.setHeader("Transfer-Encoding", "chunked");
        OutputStream os = response.getOutputStream();
        handler.handle(os);
        os.flush();
        os.close();
    }

    private static void checkArgs(HttpServletResponse response, OutputStreamHandler handler, String filename) {
        if (response == null)
            throw new RuntimeException("Http Download must have response !");
        if (handler == null)
            throw new RuntimeException("Http Download must have OutputStreamHandler !");
        if (StringUtils.isEmpty(filename))
            throw new RuntimeException("Http Download file must have name !");
    }

    public interface OutputStreamHandler {

        void handle(OutputStream os) throws IOException;

    }

}
