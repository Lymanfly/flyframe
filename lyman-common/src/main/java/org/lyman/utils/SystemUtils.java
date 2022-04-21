package org.lyman.utils;

public class SystemUtils {

    public static final String SUPER_USER = "SYSTEM";

    public static boolean getPropertyBoolean(String key) {
        return Boolean.parseBoolean(System.getenv(key));
    }

    public static String getPropertyString(String key) {
        return getPropertyString(key, null);
    }

    public static String getPropertyString(String key, String orElse) {
        String property = System.getenv(key);
        if (property == null && orElse != null)
            return orElse;
        return property;
    }

}
