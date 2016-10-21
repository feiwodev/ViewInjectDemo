package com.zeno.viewinject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Zeno on 2016/10/21.
 *
 * View inject
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface VInjector {
    int value();
}
