package com.zeno.viewinject.apt;

import com.zeno.viewinject.generate.IGenerateAdapter;
import com.zeno.viewinject.generate.ViewGenerateAdapter;
import com.zeno.viewinject.handler.IAnnotationHandler;
import com.zeno.viewinject.handler.VInjectHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by Zeno on 2016/10/21.
 *
 * Inject in View annotation processor
 *
 * 需要在配置文件中指定处理类 resources/META-INF/services/javax.annotation.processing.Processor
 * com.zeno.viewinject.apt.VInjectProcessor
 */

@SupportedAnnotationTypes("com.zeno.viewinject.annotation.VInjector")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class VInjectProcessor extends AbstractProcessor {

    List<IAnnotationHandler> mAnnotationHandler = new ArrayList<>();
    Map<String,List<VariableElement>> mHandleAnnotationMap = new HashMap<>();
    private IGenerateAdapter mGenerateAdapter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // init annotation handler
        registerHandler(new VInjectHandler());

        // init generate adapter
        mGenerateAdapter = new ViewGenerateAdapter(processingEnv);

    }

    protected void registerHandler(IAnnotationHandler handler) {
        mAnnotationHandler.add(handler);
    }

    // annotation into process run
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (IAnnotationHandler handler : mAnnotationHandler) {
            // attach environment , 关联环境
            handler.attachProcessingEnvironment(processingEnv);
            // handle annotation 处理注解 ，得到注解类的属性列表
            mHandleAnnotationMap.putAll(handler.handleAnnotation(roundEnv));

        }
        // 生成辅助类
//        mGenerateAdapter.generate(mHandleAnnotationMap);
        // 表示处理
        return true;
    }
}
