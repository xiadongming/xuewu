package com.itchina.base;

import java.util.Set;

import org.springframework.data.domain.Sort;

import com.google.common.collect.Sets;

/**
 * 排序生成器
 * Created by 瓦力.
 */
public class HouseSort {
    public static final String default_sort_key = "lastUpdateTime";
    public static final String distance_to_subway_key = "distanceToSubway";


    private static final Set<String> sort_keys = Sets.newHashSet(
            default_sort_key,
            distance_to_subway_key,
            "createTime",
            "price",
            "area"
    );

    public static Sort generateSort(String key, String directionKey) {
        key = getSortKey(key);
        Sort.Direction direction = Sort.Direction.fromStringOrNull(directionKey);
        if (null == direction) {
            direction = Sort.Direction.DESC;
        }
        return new Sort(direction, key);
    }

    public static String getSortKey(String key) {
        if (!sort_keys.contains(key)) {
            key = default_sort_key;
        }
        return key;
    }

}
