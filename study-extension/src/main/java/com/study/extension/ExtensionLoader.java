package com.study.extension;

import com.study.common.lock.Lock;
import com.study.common.utils.OrderedObject;
import com.study.common.utils.ReflectionUtils;
import com.study.extension.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.beans.Introspector;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @Author: zj
 * @Date: 2021/4/29 17:27
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class ExtensionLoader<T> {
    private static final String EXTENSION_CONFIG_FILE_ENCODING = "utf-8";

    /**
     * Service directory, this is JDK ServiceLoader's search path
     */
    private static final String SERVICES_DIRECTORY = "META-INF/services/";

    /**
     * Extension loaders, key:extension interface, value:extension loader
     */
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    /**
     * Extension instances, key: extension implement class,value:extension instance. This cache ensures one implement
     * class can only be instantiated once,this is useful when one java class implements more than one extension
     * interfaces
     */
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    // ========== instance fields ==========

    /**
     * Type,its value represents an SPI
     */
    private final Class<T> type;

    /**
     * Extension config
     */
    private volatile ExtensionConfig extensionConfig;

    /**
     * Concrete extension instances
     */
    private final ConcurrentMap<String, T> concreteExtensionInstances = new ConcurrentHashMap<>();

    // ==========  methods ==========

    private ExtensionLoader(Class<T> type) {
        this.type = type;
        loadExtensionConfig();
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Type(" + type + ") is not interface!");
        }
        if (!type.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException(
                    "Type(" + type + ") is not extension interface, because WITHOUT @" + SPI.class.getSimpleName()
                            + " Annotation!");
        }
        ExtensionLoader<?> loader = EXTENSION_LOADERS.get(type);
        if (loader == null) {
            synchronized (Lock.getLock(type.getName())) {
                loader = EXTENSION_LOADERS.get(type);
                if (loader == null) {
                    EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<>(type));
                    loader = EXTENSION_LOADERS.get(type);
                }
            }
        }
        return (ExtensionLoader<T>) loader;
    }

    private void loadExtensionConfig() {
        ExtensionConfig extensionConfig = new ExtensionConfig();
        loadDirectory(extensionConfig, SERVICES_DIRECTORY);

        this.extensionConfig = extensionConfig;
    }

    private void loadDirectory(ExtensionConfig extensionConfig, String dir) {
        String fileName = dir + type.getName();
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            Enumeration<URL> urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL resourceURL = urls.nextElement();
                    loadResource(extensionConfig, classLoader, resourceURL);
                }
            }
        } catch (Throwable t) {
            log.error("Exception when load extension class(interface: " + type
                    + ",description file: " + fileName + ")", t);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadResource(ExtensionConfig extensionConfig, ClassLoader classLoader, URL resourceURL) {
        InputStream is = null;
        try {
            is = resourceURL.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, EXTENSION_CONFIG_FILE_ENCODING));
            String line;
            while ((line = reader.readLine()) != null) {
                int commentCi = line.indexOf('#');
                if (commentCi >= 0) {
                    line = line.substring(0, commentCi);
                }
                line = line.trim();
                if (line.length() > 0) {
                    String name = null;
                    String implClassFullyQualifiedName;
                    int equalCi = line.indexOf('=');
                    if (equalCi > 0) {
                        name = line.substring(0, equalCi).trim();
                        implClassFullyQualifiedName = line.substring(equalCi + 1);
                    } else {
                        implClassFullyQualifiedName = line;
                    }
                    ImplementConfig implementConfig = createImplementConfig(implClassFullyQualifiedName.trim(),
                            classLoader, name);
                    extensionConfig.addImplementConfig(implementConfig);
                }
            }
        } catch (Throwable t) {
            log.error("Exception when load extension class,interface: " + type + ",class file: " + resourceURL, t);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    private ImplementConfig createImplementConfig(String implClassFullyQualifiedName,
                                                  ClassLoader classLoader, String name) {
        Class<?> implementClass;
        try {
            implementClass = Class.forName(implClassFullyQualifiedName, true, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (!type.isAssignableFrom(implementClass)) {
            throw new IllegalStateException(
                    "Class " + implementClass.getName() + "is not subtype of interface");
        }

        SPIImplement spiImplAnnotation = implementClass.getAnnotation(SPIImplement.class);
        if (spiImplAnnotation == null) {
            throw new IllegalArgumentException(
                    "Implement(" + implementClass + ") is not extension, because WITHOUT @" + SPIImplement.class
                            .getSimpleName() + " Annotation!");
        }

        ImplementType implementType;
        if (implementClass.isAnnotationPresent(Adaptive.class)) {
            implementType = ImplementType.ADAPTIVE;
        } else if (isWrapperClass(implementClass)) {
            implementType = ImplementType.WRAPPER;
        } else {
            implementType = ImplementType.CONCRETE;
        }

        if (StringUtils.isBlank(name)) {
            // get name from annotation
            name = spiImplAnnotation.name();
        }
        if (StringUtils.isBlank(name)) {
            // generate from class name
            String classShortName = ReflectionUtils.getShortName(implementClass.getName());
            name = Introspector.decapitalize(classShortName);
        }

        return new ImplementConfig(implementType, implementClass, spiImplAnnotation, name,
                spiImplAnnotation.warehouseIds(), spiImplAnnotation.order());
    }

    private boolean isWrapperClass(Class<?> clazz) {
        try {
            clazz.getConstructor(type);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * Extension config
     */
    @Data
    private class ExtensionConfig {
        private final Map<String, ImplementConfigSet> concreteImplements = new HashMap<>();
        private final ImplementConfigSet wrapperImplements = new ImplementConfigSet();
        private final ImplementConfigSet adaptiveImplements = new ImplementConfigSet();

        ImplementConfig indexConcreteImplement(String name, Class<?> implClass) {
            return concreteImplements.get(name).configs.stream().filter(
                    e -> e.getImplementClass().equals(implClass)).findFirst().orElseThrow(() -> new RuntimeException(""));
        }

        private void addImplementConfig(ImplementConfig implementConfig) {
            ImplementType implementType = implementConfig.getImplementType();
            if (implementType == ImplementType.ADAPTIVE) {
                adaptiveImplements.add(implementConfig);
            } else if (implementType == ImplementType.CONCRETE) {
                concreteImplements.computeIfAbsent(implementConfig.getName(),
                        k -> new ImplementConfigSet()).add(implementConfig);
            } else if (implementType == ImplementType.WRAPPER) {
                wrapperImplements.add(implementConfig);
            } else {
                throw new RuntimeException("");
            }
        }
    }

    @Data
    @EqualsAndHashCode
    private static class ImplementConfig {
        private final ImplementType implementType;
        private final Class<?> implementClass;
        private final SPIImplement spiImplAnnotation;
        private final String name;
        private final String warehouseIds;
        private final int order;

        ImplementConfig(ImplementType implementType, Class<?> implementClass,
                        SPIImplement spiImplAnnotation, String name, String warehouseIds, int order) {
            this.implementType = implementType;
            this.implementClass = implementClass;
            this.spiImplAnnotation = spiImplAnnotation;
            this.name = name;
            this.warehouseIds = warehouseIds;
            this.order = order;
        }

        boolean canApply(String warehouseId) {
            String warehouseIds = this.getWarehouseIds();
            return SPIImplement.PRODUCT_APPLY_ALL.equalsIgnoreCase(warehouseIds) || warehouseIds.contains(warehouseId);
        }
    }

    private enum ImplementType {
        /**
         * ADAPTIVE
         */
        ADAPTIVE,
        /**
         * CONCRETE
         */
        CONCRETE,
        /**
         * WRAPPER
         */
        WRAPPER
    }

    private class ImplementConfigSet {
        Set<ImplementConfig> configs = new HashSet<>();

        void add(ImplementConfig implementConfig) {
            configs.add(implementConfig);
        }

        boolean accept(String warehouseId) {
            return configs.stream().anyMatch(e -> e.canApply(warehouseId));
        }

        List<ImplementConfig> getAllImplements(String warehouseId) {
            return configs.stream().filter(e -> e.canApply(warehouseId)).collect(Collectors.toList());
        }

        ImplementConfig getSoloImplement(String warehouseId) {
            List<ImplementConfig> allImplements = getAllImplements(warehouseId);
            List<ImplementConfig> warehouseSpecifiedImplements = allImplements.stream().filter(
                    e -> !e.getWarehouseIds().equalsIgnoreCase(SPIImplement.PRODUCT_APPLY_ALL)).collect(
                    Collectors.toList());

            // warehouse level customization should override product extension if exists
            if (!warehouseSpecifiedImplements.isEmpty()) {
                if (warehouseSpecifiedImplements.size() == 1) {
                    return warehouseSpecifiedImplements.get(0);
                }
                throw new IllegalStateException("Wrong state,warehouseId=[" + warehouseId + "],type=[" + type + "]");
            } else {
                if (allImplements.size() == 1) {
                    return allImplements.get(0);
                }
                throw new IllegalStateException("Wrong state,warehouseId=[" + warehouseId + "],type=[" + type + "]");
            }
        }
    }

    public T getAdaptiveExtension() {
        String warehouseId = "1";
        Class<?> adaptiveImplement = extensionConfig.getAdaptiveImplements().getSoloImplement(warehouseId).getImplementClass();
        return get0(adaptiveImplement);
    }

    @SuppressWarnings("unchecked")
    private T get0(Class<?> implementClass) {
        T instance = (T) EXTENSION_INSTANCES.get(implementClass);
        if (instance == null) {
            synchronized (this) {
                instance = (T) EXTENSION_INSTANCES.get(implementClass);
                if (instance == null) {
                    try {
                        EXTENSION_INSTANCES.putIfAbsent(implementClass, init(injectDependency((T) implementClass.newInstance())));
                    } catch (Exception e) {
                        throw new IllegalStateException("Can not create extension ", e);
                    }
                    instance = (T) EXTENSION_INSTANCES.get(implementClass);
                }
            }
        }
        return instance;
    }

    private T injectDependency(T instance) {
        if (type != DependencyFinder.class) {
            try {
                // process methods declared injection
                for (Method method : instance.getClass().getMethods()) {
                    if (method.getName().startsWith("set") && method.getParameterTypes().length == 1 && Modifier
                            .isPublic(method.getModifiers())) {
                        InjectMethod injectAnnotation = method.getAnnotation(InjectMethod.class);
                        if (injectAnnotation == null) {
                            continue;
                        }
                        // inject
                        String dependencyName = injectAnnotation.value();
                        if (dependencyName.trim().length() == 0) {
                            dependencyName = method.getName().length() > 3 ? method.getName().substring(3, 4)
                                    .toLowerCase() + method.getName().substring(4) : "";
                        }
                        Class<?> dependencyType = method.getParameterTypes()[0];
                        try {
                            Object dependency = ExtensionLoader.getExtensionLoader(DependencyFinder.class)
                                    .getAdaptiveExtension().find(dependencyType, dependencyName);
                            if (dependency != null) {
                                method.invoke(instance, dependency);
                            }
                        } catch (Exception e) {
                            log.error("fail to inject via method " + method.getName()
                                    + "|interface " + this.type.getName() + "|" + e.getMessage(), e);
                        }
                    }
                }

                // process fields declared injection
                List<Field> resourceFields = Arrays.stream(
                        ReflectionUtils.getDeclaredFieldsIncludingAncestors(instance.getClass())).filter(
                        e -> e.getAnnotation(Resource.class) != null).collect(Collectors.toList());
                for (Field resourceField : resourceFields) {
                    Resource resourceAnnotation = resourceField.getAnnotation(Resource.class);
                    String dependencyName = resourceAnnotation.name();
                    if (dependencyName.trim().length() == 0) {
                        dependencyName = resourceField.getName();
                    }
                    Class<?> dependencyType = resourceField.getType();
                    try {
                        Object dependency = ExtensionLoader.getExtensionLoader(DependencyFinder.class)
                                .getAdaptiveExtension().find(dependencyType, dependencyName);
                        if (dependency != null) {
                            resourceField.setAccessible(true);
                            resourceField.set(instance, dependency);
                        }
                    } catch (Exception e) {
                        log.error("fail to inject via field " + resourceField.getName()
                                + "|interface " + this.type.getName() + "|" + e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return instance;
    }

    private T init(T instance) {
        for (Method method : instance.getClass().getMethods()) {
            InitMethod initAnnotation = method.getAnnotation(InitMethod.class);
            if (initAnnotation != null) {
                // find init method,invoke it
                try {
                    method.invoke(instance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
        return instance;
    }

    public Set<String> getNames() {
        String warehouseId = "1";
        return extensionConfig.getConcreteImplements().entrySet().stream().filter(
                e -> e.getValue().accept(warehouseId)).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    public T getByName(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Extension name is empty");
        }
        String warehouseId = "1";
        Class<?> concreteImplement = extensionConfig.getConcreteImplements().get(name).getSoloImplement(warehouseId)
                .getImplementClass();
        if (concreteImplement == null) {
            throw new IllegalStateException(
                    "Extension not found,warehouseId=[" + warehouseId + "],type=[" + type + "]");
        }

        String concreteExtensionInstanceCacheKey = warehouseId + "|" + concreteImplement.getCanonicalName();
        T instance = concreteExtensionInstances.get(concreteExtensionInstanceCacheKey);
        if (instance == null) {
            synchronized (this) {
                instance = concreteExtensionInstances.get(concreteExtensionInstanceCacheKey);
                if (instance == null) {
                    concreteExtensionInstances.putIfAbsent(concreteExtensionInstanceCacheKey,
                            createConcreteExtension(warehouseId, concreteImplement));
                    instance = concreteExtensionInstances.get(concreteExtensionInstanceCacheKey);
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private T createConcreteExtension(String warehouseId, Class<?> concreteImplClass) {
        try {
            T instance = get0(concreteImplClass);
            //wrap
            List<ImplementConfig> wrapperImplements = extensionConfig.getWrapperImplements().getAllImplements(warehouseId);
            if (wrapperImplements != null && !wrapperImplements.isEmpty()) {
                for (ImplementConfig implementConfig : wrapperImplements) {
                    instance = injectDependency((T) implementConfig.implementClass.getConstructor(type).newInstance(instance));
                }
            }
            return instance;
        } catch (Throwable t) {
            throw new IllegalStateException("Can not create extension ", t);
        }
    }

    public List<T> list() {
        return this.getNames().stream().map(this::getByName).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<T> listAndSort() {
        return this.getNames().stream().map(e -> {
            T extension = getByName(e);
            ImplementConfig config = this.extensionConfig.indexConcreteImplement(e, extension.getClass());
            return new OrderedObject(extension, config.getOrder());
        }).sorted().map(e -> (T) e.getObj()).collect(Collectors.toList());
    }
}
