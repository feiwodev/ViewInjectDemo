package com.zeno.viewinject;


import android.app.Activity;

import com.zeno.viewinject.adapter.IVInjectorAdapter;

import java.util.HashMap;
import java.util.Map;

public class ViewInject {

    public static final String SUFFIX = "$VInjectorAdapter";

    static Map<Class<? extends Activity>,IVInjectorAdapter<?>> mInjectorAdapter = new HashMap();

    public static void inject(Activity activity) {
        IVInjectorAdapter<Object> vInjectorAdapter = getVInjectorAdapter(activity.getClass());
        vInjectorAdapter.injects(activity);
    }

    private static <T>IVInjectorAdapter<T> getVInjectorAdapter(Class<? extends Activity> clazz){
        IVInjectorAdapter<T> injectorAdapter = (IVInjectorAdapter<T>) mInjectorAdapter.get(clazz);
        if (injectorAdapter != null){
            return injectorAdapter;
        }
        String adapterClassName = clazz.getName()+SUFFIX;

        try{
            injectorAdapter = (IVInjectorAdapter<T>) Class.forName(adapterClassName).newInstance();
            mInjectorAdapter.put(clazz,injectorAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
        return injectorAdapter;
    }
}
