package com.zeno.viewinject.utils;


import android.app.Activity;
import android.view.View;

/**
 * Created by Zeno on 2016/10/22.
 *
 * 模式代码 ，根据id获取组件对象
 */
public final class ViewFinder {

    public static <T extends View> T findViewById(Activity activity,int id){
        return (T)activity.findViewById(id);
    }
}
