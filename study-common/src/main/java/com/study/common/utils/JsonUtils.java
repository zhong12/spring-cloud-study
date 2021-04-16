package com.study.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.study.common.exception.BizException;
import com.study.common.exception.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author: zj
 * @Date: 2021/4/15 14:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class JsonUtils {
    /**
     * 对象转json
     *
     * @param t
     * @return
     */
    public static <T> String parseJson(T t) {
        if (null == t) {
            log.error("JsonUtil.parseJson error: object is null");
            throw new BizException(ErrorEnum.PARAM_NOT_EXIST);
        }
        return JSON.toJSONString(t);
    }

    /**
     * json转对象
     *
     * @param json
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            log.error("JsonUtil.parseObject error: json is null");
            throw new BizException(ErrorEnum.PARAM_NOT_EXIST);
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        return JSON.toJavaObject(jsonObject, clazz);
    }


    /**
     * str 转 json
     *
     * @param str
     * @return
     */
    public static JSONObject parseJson(String str) {
        if (StringUtils.isEmpty(str)) {
            log.error("JsonUtil.parseObject error: str is null");
            throw new BizException(ErrorEnum.JSON_ERROR);
        }
        return JSONObject.parseObject(str);
    }


    /**
     * json转hashmap
     *
     * @param json
     * @return
     */
    public static <K, V> Map<K, V> jsonToHashMap(String json) {
        if (StringUtils.isEmpty(json)) {
            log.error("JsonUtil.jsonToHashMap error: json is null");
            throw new BizException(ErrorEnum.JSON_ERROR);
        }
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<K, V> params = JSONObject.parseObject(jsonObject.toJSONString(),
                new TypeReference<Map<K, V>>() {
                });
        return params;
    }

    /**
     * 对象通过json转map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objToMap(Object obj) {
        if (null == obj) {
            log.error("JsonUtil.objToMap error: obj is null");
            throw new BizException(ErrorEnum.JSON_ERROR);
        }
        String json = parseJson(obj);
        return parseObject(json, Map.class);
    }


    /**
     * 获取指定key的JSONObject
     *
     * @param str
     * @return
     */
    public static JSONObject parseByKeyJsonObject(String key, String str) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(key)) {
            log.error("JsonUtil.parseByKeyJsonObject error: str/key is null");
            throw new BizException(ErrorEnum.JSON_ERROR);
        }
        JSONObject jsonObject = parseJson(str);
        if (!jsonObject.isEmpty()) {
            if (jsonObject.get(key) instanceof JSONArray) {
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                if (!jsonArray.isEmpty()) {
                    for (Object object : jsonArray) {
                        parseByKeyJsonObject(key, String.valueOf(object));
                    }
                }
            } else if (jsonObject.get(key) instanceof JSONObject) {
                jsonObject = jsonObject.getJSONObject(key);
            }
        }
        return jsonObject;
    }
}
