package com.study.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:39
 * @Description:
 * @Version: 1.0
 */
public class ReflectionUtils {
    /**
     * The package separator character: '.'
     */
    public static final char PACKAGE_SEPARATOR = '.';

    /**
     * The CGLIB class separator: "$$"
     */
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * The inner class separator character: '$'
     */
    public static final char INNER_CLASS_SEPARATOR = '$';

    public static Field[] getDeclaredFieldsIncludingAncestors(Class<?> clazz) {
        // introspect hierarchy tree,no dead loop
        List<Field> fieldList = new ArrayList<>();
        Class current = clazz;
        while (current != null) {
            fieldList.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fieldList.toArray(new Field[fieldList.size()]);
    }

    public static String getShortName(String className) {
        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }
        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
        return shortName;
    }
}
