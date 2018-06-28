package com.stone.base;

import java.lang.Exception;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Joseph.Yan.
 */
public class GMInvoker<T> {
    private static final String TAG = "GMInvoker--->";

    private Class<T> tClass;
    private String methodName;
    private T t;
    private Set<Object> objects;

    public static <T> GMInvoker<T> newInstance(Class<T> tClass) {
        return newInstance(tClass, null);
    }

    public static <T> GMInvoker<T> newInstance(Class<T> tClass, String methodName) {
        ArgsUtils.notNull(tClass, "Type of class");
        return new GMInvoker<>(tClass, methodName);
    }

    private GMInvoker(Class<T> tClass, String methodName) {
        this.tClass = tClass;
        if (ArgsUtils.isEmpty(methodName)) {
            this.methodName = "set" + this.tClass.getSimpleName();
        } else {
            this.methodName = methodName;
        }
        this.objects = new HashSet<>();
    }

    public GMInvoker<T> wrap(Object... objects) {
        List<Object> objectList = Arrays.asList(objects);
        this.objects.addAll(objectList);
        makeInvoke(objectList);
        return this;
    }

    public GMInvoker<T> set(T t) {
        this.t = t;
        makeInvoke(objects);
        return this;
    }

    private void makeInvoke(Collection<Object> objects) {
        if (objects == null || objects.size() == 0) {
            return;
        }
        for (Object object : objects) {
            invoke(object, methodName, tClass, t);
        }
    }

    private static <T> void invoke(Object object, String methodName, Class<T> tClass, T t) {
        Class<?> cls = object.getClass();
        try {
            Method method = cls.getMethod(methodName, tClass);
            method.invoke(object, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
