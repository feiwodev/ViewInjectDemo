package com.zeno.viewinject.handler;


import com.zeno.viewinject.annotation.VInjector;
import com.zeno.viewinject.utils.AnnotationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by Zeno on 2016/10/21.
 *
 * 注解处理实现 , 解析VInjector注解属性
 */
public class VInjectHandler implements IAnnotationHandler {


    private ProcessingEnvironment mProcessingEnvironment;

    @Override
    public void attachProcessingEnvironment(ProcessingEnvironment environment) {
            this.mProcessingEnvironment = environment;
    }

    @Override
    public Map<String, List<VariableElement>> handleAnnotation(RoundEnvironment roundEnvironment) {
        Map<String,List<VariableElement>> map = new HashMap<>();
        /*获取一个类中带有VInjector注解的属性列表*/
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(VInjector.class);
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            /*获取类名 ，将类目与属性配对，一个类，对于他的属性列表*/
            String className = getFullClassName(variableElement);
            List<VariableElement> cacheElements = map.get(className);
            if (cacheElements == null) {
                cacheElements = new ArrayList<>();
                map.put(className,cacheElements);
            }
            cacheElements.add(variableElement);
        }

        return map;
    }

    /**
     * 获取注解属性的完整类名
     * @param variableElement
     */
    private String getFullClassName(VariableElement variableElement) {
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        String packageName = AnnotationUtils.getPackageName(mProcessingEnvironment,typeElement);
        return packageName+"."+typeElement.getSimpleName().toString();
    }
}
