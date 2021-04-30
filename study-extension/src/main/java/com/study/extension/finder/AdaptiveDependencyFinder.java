package com.study.extension.finder;

import com.study.extension.Constants;
import com.study.extension.DependencyFinder;
import com.study.extension.ExtensionLoader;
import com.study.extension.annotation.Adaptive;
import com.study.extension.annotation.SPI;
import com.study.extension.annotation.SPIImplement;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:25
 * @Description: Adaptive dependency finder
 * @Version: 1.0
 */
@Adaptive
@SPIImplement
public class AdaptiveDependencyFinder implements DependencyFinder {
    @Override
    public <T> T find(Class<T> type, String name) {
        ExtensionLoader<DependencyFinder> loader = ExtensionLoader.getExtensionLoader(DependencyFinder.class);
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            return loader.getByName(Constants.DEPENDENCY_FINDER_NAME_EXTENSION).find(type, name);
        }
        // find by other finders
        for (String extensionName : loader.getNames()) {
            if (!extensionName.equals(Constants.DEPENDENCY_FINDER_NAME_EXTENSION)) {
                T object = loader.getByName(extensionName).find(type, name);
                if (object != null) {
                    return object;
                }
            }
        }
        return null;
    }
}
