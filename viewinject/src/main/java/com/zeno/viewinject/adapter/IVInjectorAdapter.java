package com.zeno.viewinject.adapter;

/**
 * Created by Zeno on 2016/10/22.
 *
 * 辅助类的父接口 , T 代表注入的目标类型，ViewInject注入目标为Activity
 */
public interface IVInjectorAdapter<T> {
    void injects(T target);
}
