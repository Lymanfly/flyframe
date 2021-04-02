package org.lyman.utils;

public class StringUtils extends org.springframework.util.StringUtils {

    public static final String EMPTY_STR = "";

    public static final String SLASH = "/";

    public static final String DOUBLE_SLASH = "//";

    public static final String BACKSLASH = "\\";

    public static final String BAR_STR = "-";

    public static final String COLON = ":";

    public static final String COMMA = ",";

    public static final String SEMICOLON = ";";

    public static final String SPACE = " ";

    public static final String WEIRD_SPACE = new String(new byte[] {-29, -128, -128});

    public static boolean isNotEmpty(String str) {
        return str != null && !"".equals(str);
    }

}
