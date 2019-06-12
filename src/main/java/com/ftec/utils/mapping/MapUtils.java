package com.ftec.utils.mapping;

import java.util.Map;
import java.util.stream.Collectors;

public class MapUtils {

    public static String mapToString(Map<String, String[]> map) {
        return map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(", "));
    }
}
