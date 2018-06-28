package com.stone.mvp;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.stone.base.ArgsUtils;
import com.stone.base.exception.NotFoundException;
import com.stone.base.exception.RepeatException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * Created by Joseph.Yan.
 */

public abstract class Presenter<V extends IView> implements IPresenter<V> {

    private Context context;
    private ViewProxy<V> viewProxy;

    private void create(Context context, ViewProxy<V> viewProxy) {
        this.context = context;
        this.viewProxy = viewProxy;
        onCreate();
    }

    protected void onCreate() {
    }

    @Override
    public final void bind(V view) {
        viewProxy.bind(view);
    }

    @Override
    public final void unBind() {
        viewProxy.unBind();
    }

    private void destroy() {
        unBind();
        this.context = null;
        this.viewProxy = null;
        onDestroy();
    }

    protected void onDestroy() {
    }

    @Override
    public final Context getContext() {
        return context;
    }

    @Override
    public final V getView() {
        return viewProxy == null ? null : viewProxy.proxy();
    }

    public static <V extends IView, P extends Presenter<V>> P inflate(Context context, Class<P> pClass) {
        ArgsUtils.notNull(context, "Context must not be null");
        ArgsUtils.notNull(pClass, "Class of presenter must not be null");
        try {
            Class<V> vClass = getClass4V(pClass);
            P pImpl = pClass.newInstance();
            ((Presenter<V>) pImpl).create(context, new ViewProxy<>(vClass));
            return pImpl;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void destroy(Presenter presenter) {
        if (presenter != null) {
            presenter.destroy();
        }
    }

    @SuppressWarnings("unchecked")
    private static <V extends IView> Class<V> getClass4V(Class<? extends Presenter<V>> pClass) throws NotFoundException {
        Class<?> matchClass = findMatchGenericClass(pClass);
        if (matchClass == null) {
            throw new NotFoundException("Not found matchable class");
        }
        Log.d("--->", matchClass.getName());
        ParameterizedType parameterizedType = (ParameterizedType) matchClass.getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        return (Class<V>) types[0];
    }

    private static Class<?> findMatchGenericClass(Class<?> clazz) {
        if (clazz == Object.class) {
            return null;
        }
        Class<?> superClass = clazz.getSuperclass();
        if (clazz == Object.class) {
            return null;
        }
        if (superClass.getName().equals(Presenter.class.getName())) {
            return clazz;
        }
        return findMatchGenericClass(clazz.getSuperclass());
    }

    private static class ViewProxy<V extends IView> implements InvocationHandler {

        final Class<V> vClass;

        V proxy;
        V view;

        private ViewProxy(Class<V> vClass) {
            this.vClass = vClass;
        }

        @SuppressWarnings("unchecked")
        private V proxy() {
            if (proxy == null) {
                proxy = (V) Proxy.newProxyInstance(vClass.getClassLoader(),
                        new Class[]{vClass}, this);
            }
            return proxy;
        }

        private void bind(V view) {
            if (this.view != null) {
                throw new RepeatException("Repeat bind");
            }
            this.view = view;
        }

        private void unBind() {
            this.view = null;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new RuntimeException("Calling must touch main-thread" + method.getName());
            }
            if (view != null) {
                return method.invoke(view, args);
            }
            return null;
        }
    }
}
