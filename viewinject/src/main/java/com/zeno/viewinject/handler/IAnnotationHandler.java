package com.zeno.viewinject.handler;

import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.VariableElement;

/**
 * Created by Zeno on 2016/10/21.
 *
 * annotation handler
 */
public interface IAnnotationHandler {

    /**
     *  attch environment
     * @param environment 处理环境
     */
    void attachProcessingEnvironment(ProcessingEnvironment environment) ;

    /**
     * handle annotation 处理注解 ，生成辅助类的时候需要用到注解的类型
     * @param roundEnvironment 周边环境
     * @return <p>
     *     Map<String,List<VariableElement>>
     *     key ---> 表示是哪个类的注入 （宿主）
     *     value ---> 表示注解类的注入的字段属性列表 （注解字段）
     * </p>
     */
    Map<java.lang.String, List<VariableElement>> handleAnnotation(RoundEnvironment roundEnvironment);
}
