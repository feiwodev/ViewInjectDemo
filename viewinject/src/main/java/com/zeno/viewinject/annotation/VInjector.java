package com.zeno.viewinject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Zeno on 2016/10/21.
 *
 * View inject
 * 字段注入注解，可以新建多个注解，再通过AnnotationProcessor进行注解处理
 * RetentionPolicy.CLASS ，在编译的时候进行注解 。我们需要在生成.class文件的时候需要进行处理
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface VInjector {
    int value();
}
