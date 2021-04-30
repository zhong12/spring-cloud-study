package com.study.extension.finder;

import com.study.extension.DependencyFinder;
import com.study.extension.ExtensionLoader;
import com.study.extension.annotation.SPI;
import com.study.extension.annotation.SPIImplement;

/**
 * @Author: zj
 * @Date: 2021/4/29 18:37
 * @Description: Extension dependency finder
 * @Version: 1.0
 */
@SPIImplement
public class ExtensionDependencyFinder implements DependencyFinder {
    @Override
    public <T> T find(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            // find adaptive
            ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
            if (!loader.getNames().isEmpty()) {
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }
}
