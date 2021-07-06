package org.lyman.utils;

import java.util.UUID;

public class CodecUtils {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
