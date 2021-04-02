package org.lyman.utils;

import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.stream.Stream;

public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

    public static Stream stream(@NonNull Object[] objects) {
        return Arrays.stream(objects);
    }

}
