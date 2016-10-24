package com.zeno.viewinject.utils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * Created by Zeno on 2016/10/22.
 */
public class AnnotationUtils {

    /**
     * 根据类型获取包名
     * @param env
     * @param typeElement
     * @return
     */
    public static String getPackageName(ProcessingEnvironment env, TypeElement typeElement) {
        return env.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
    }
}
