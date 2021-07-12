package org.lyman.utils;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils extends org.springframework.util.CollectionUtils {

    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return map != null && map.size() > 0;
    }

    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

}
