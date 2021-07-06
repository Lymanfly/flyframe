package org.lyman.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WebUtils extends org.springframework.web.util.WebUtils {

    private static final String UNKNOWN = "unknown";

    private static final String[] HEADER_IP_KEYS = new String[] {
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "http-client-ip",
            "HTTP_X_FORWARD_FOR"
    };

    public static String getApplication(HttpServletRequest request) {
        Assert.notNull(request, "Request must not be null");
        String cp = request.getContextPath();
        return Objects.isNull(cp) ? StringUtils.EMPTY_STR : cp;
    }

    public static String getApplicationPath(HttpServletRequest request) {
        Assert.notNull(request, "Request must not be null");
        String cp = getApplication(request);
        return getHostPath(request).concat(cp);
    }

    public static String getHostPath(HttpServletRequest request) {
        Assert.notNull(request, "Request must not be null");
        String scheme = request.getScheme(), sn = request.getServerName();
        Integer sp = request.getRemotePort();
        return String.join(StringUtils.COLON, scheme, StringUtils.DOUBLE_SLASH.concat(sn), sp.toString());
    }

    public static String getUrlWithArgs(HttpServletRequest request) {
        return getUrlWithArgs(request, false);
    }

    public static String getUrlWithArgs(HttpServletRequest request, boolean encodeBase64) {
        Assert.notNull(request, "Request must not be null");
        String url = request.getRequestURL().toString(), qs = request.getQueryString();
        if (!StringUtils.isEmpty(qs))
            url = url.concat(qs);
        if (encodeBase64)
            url = Base64.encodeBase64URLSafeString(url.getBytes());
        return url;
    }

    public static String getRelativeUrl(HttpServletRequest request) {
        Assert.notNull(request, "Request must not be null");
        String uri = request.getRequestURI(), cp = getApplication(request);
        if (!Objects.isNull(cp) && uri.startsWith(cp)) {
            uri = uri.substring(cp.length());
            if (!uri.startsWith(StringUtils.SLASH))
                uri = StringUtils.SLASH.concat(uri);
        }
        return uri;
    }

    public static Map<String, String> getRequestArgs(HttpServletRequest request) {
        String qs = request.getQueryString();
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotEmpty(qs)) {
            String[] arr = qs.split("&");
            for (String pair : arr) {
                String[] couple = pair.split("=");
                if (couple.length == 2)
                    map.put(couple[0], couple[1]);
            }
        }
        return map;
    }

    public static String getUserAgent(HttpServletRequest request) {
        Assert.notNull(request, "Request must not be null");
        String ua = request.getHeader("user-agent");
        return Objects.isNull(ua) ? UNKNOWN : ua;
    }

    public static String getUserBrowser(HttpServletRequest request) {
        String ua = getUserAgent(request);
        if (!UNKNOWN.equals(ua)) {
            int index = ua.indexOf("Chrome");
            if (index > -1)
                return ua.substring(index).split(StringUtils.SEMICOLON)[0];
            index = ua.indexOf("Firefox");
            if (index > -1)
                return ua.substring(index).split(StringUtils.SPACE)[0];
            index = ua.indexOf("MSIE");
            if (index > -1)
                return ua.substring(index).split(StringUtils.SPACE)[0];
        }
        return ua;
    }

    public static String getClientAddress(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        for (int i = 0, len = HEADER_IP_KEYS.length; i < len && StringUtils.isEmpty(ip); i++)
            ip = request.getHeader(HEADER_IP_KEYS[i]);
        if (!StringUtils.isEmpty(ip) && ip.contains(StringUtils.COMMA))
            ip = ip.substring(0, ip.indexOf(StringUtils.COMMA)).trim();
        return ip;
    }

}
