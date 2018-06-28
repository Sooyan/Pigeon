package com.stone.mvp;

import android.content.Context;

/**
 * Created by Joseph.Yan.
 */
public interface IPresenter<V extends IView> {

    Context getContext();

    V getView();

    void bind(V view);

    void unBind();
}
