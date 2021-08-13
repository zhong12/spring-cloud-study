package com.github.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.LRUMap;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @Classname LRUMapUtils
 * @Description LRUMap
 * @Date 2021/8/12 17:27
 * @Created by Z.J
 */
@Slf4j
public class LRUMapUtils {

    /**
     * add
     * If it does not exist, create; Add when present
     *
     * @param lruMap
     * @param maxSize maxSize is valid when lruMap equals null
     * @return
     */
    private static <K, V> LRUMap<K, V> check(LRUMap<K, V> lruMap, int maxSize) {
        lruMap = Optional.ofNullable(lruMap).orElse(maxSize > 0 ? new LRUMap<>(maxSize) : new LRUMap<>());
        return lruMap;
    }

    /**
     * add
     * If it does not exist, create; Add when present
     *
     * @param lruMap
     * @param key
     * @param value
     * @param maxSize
     * @return
     */
    private static <K, V> LRUMap<K, V> add(LRUMap<K, V> lruMap, K key, V value, int maxSize) {
        lruMap = check(lruMap, maxSize);
        lruMap.put(key, value);
        return lruMap;
    }

    /**
     * add
     * If it does not exist, create; Add when present
     *
     * @param lruMap
     * @param maxSize maxSize is valid when lruMap equals null
     * @return
     */
    private static <K, V> LRUMap<K, V> add(LRUMap<K, V> lruMap, Map<K, V> map, int maxSize) {
        lruMap = check(lruMap, maxSize);
        lruMap.putAll(map);
        return lruMap;
    }

    /**
     * get
     *
     * @param lruMap
     * @param key
     * @return
     */
    public static <T> T getValue(LRUMap lruMap, String key) {
        return (T) lruMap.get(key);
    }


    /*****************************************String*****************************************************/
    /**
     * add String
     *
     * @param key
     * @param value
     * @return
     */
    public static LRUMap<String, String> addValue(String key, String value) {
        return addValue(null, key, value, 0);
    }

    /**
     * add String
     *
     * @param lruMap
     * @param key
     * @param value
     * @param maxSize
     * @return
     */
    public static LRUMap<String, String> addValue(LRUMap<String, String> lruMap, String key, String value, int maxSize) {
        return add(lruMap, key, value, maxSize);
    }

    /*****************************************List*****************************************************/
    /**
     * add List
     *
     * @param key
     * @param list
     * @return
     */
    public static LRUMap<String, List> addListValue(String key, List list) {
        return addListValue(null, key, list, 0);
    }

    /**
     * add List
     *
     * @param lruMap
     * @param key
     * @param value
     * @param maxSize
     * @return
     */
    public static LRUMap<String, List> addListValue(LRUMap<String, List> lruMap, String key, List value, int maxSize) {
        return add(lruMap, key, value, maxSize);
    }

    /*****************************************Set*****************************************************/
    /**
     * add Set
     *
     * @param key
     * @param set
     * @return
     */
    public static LRUMap<String, Set> addSetValue(String key, Set set) {
        return addSetValue(null, key, set, 0);
    }

    /**
     * add Set
     *
     * @param lruMap
     * @param key
     * @param set
     * @param maxSize
     * @return
     */
    public static LRUMap<String, Set> addSetValue(LRUMap<String, Set> lruMap, String key, Set set, int maxSize) {
        return add(lruMap, key, set, maxSize);
    }

    /*****************************************Map*****************************************************/
    /**
     * add Map
     *
     * @param map
     * @return
     */
    public static LRUMap<String, Map> addMapValue(Map map) {
        return addMapValue(null, map, 0);
    }

    /**
     * add map
     *
     * @param lruMap
     * @param map
     * @param maxSize
     * @return
     */
    public static LRUMap<String, Map> addMapValue(LRUMap<String, Map> lruMap, Map map, int maxSize) {
        return add(lruMap, map, maxSize);
    }


    public static void main(String[] args) {
        LRUMap<String, String> lruStr = LRUMapUtils.addValue("a", "b");
        lruStr = LRUMapUtils.addValue(new LRUMap<>(), "a", "c", lruStr.maxSize());
        log.info("lruStr={}", lruStr);
        LRUMap<String, List> lruList = new LRUMap<>();
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        lruList = LRUMapUtils.addListValue(lruList, "b", list, 4);
        log.info("lruList={}", lruList);
        LRUMap<String, Set> lruSet = new LRUMap<>();
        Set<String> set = new LinkedHashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        lruSet = LRUMapUtils.addSetValue(lruSet, "b", set, 4);
        log.info("lruSet={}", lruSet);

        LRUMap<String, Map> lruMap = new LRUMap<>(4);
        Map<String, String> map = new HashMap<>();
        map.put("a","a");
        map.put("b","b");
        map.put("c","c");
        map.put("d","d");
        log.info("lruMap={},{}", lruMap.maxSize(), lruMap);
        lruMap = LRUMapUtils.addMapValue(lruMap, map, 4);
        log.info("lruMap={},{}", lruMap, lruMap);

        String value = LRUMapUtils.getValue(lruMap, "c");
        log.info("value={}", value);

        LRUMap<String, Map> lruMap1 = new LRUMap<>();
        map.put("e","e");
        map.put("f","f");
        lruMap1= LRUMapUtils.addMapValue(lruMap1, map, 4);
        log.info("lruMap1={},{}", lruMap1, lruMap1);
    }
}
