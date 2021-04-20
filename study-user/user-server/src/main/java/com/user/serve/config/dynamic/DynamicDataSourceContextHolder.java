package com.user.serve.config.dynamic;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/20 10:27
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class DynamicDataSourceContextHolder {
    private static int counter = 0;

    /**
     * localThread
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = ThreadLocal.withInitial(DataSourceKey.master::name);

    /**
     * All DataSource List
     */
    public static List<Object> dataSourceKeys = new ArrayList<>();

    /**
     * secondDataSourceKeys
     */
    public static List<Object> secondDataSourceKeys = new ArrayList<>();

    /**
     * change DataSource
     *
     * @param
     */
    public static void setDataSourceKey(String key) {
        CONTEXT_HOLDER.set(key);
    }

    /**
     * Get master data source
     */
    public static void getMasterDataSource() {
        CONTEXT_HOLDER.set(DataSourceKey.master.name());
    }

    /**
     * Get master data source
     */
    public static void getSecondDataSource() {
        try {
            int datasourceKeyIndex = counter % secondDataSourceKeys.size();
            CONTEXT_HOLDER.set(String.valueOf(secondDataSourceKeys.get(datasourceKeyIndex)));
            counter++;
        } catch (Exception e) {
            log.error("Change slave datasource failed, error message is {}", e.getMessage());
            getMasterDataSource();
            e.printStackTrace();
        }
    }

    /**
     * Get current DataSource
     *
     * @return
     */
    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * clearDataSourceKey
     */
    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * containDataSourceKey
     *
     * @param
     * @return
     */
    public static boolean containDataSourceKey(String key) {
        return dataSourceKeys.contains(key);
    }
}
