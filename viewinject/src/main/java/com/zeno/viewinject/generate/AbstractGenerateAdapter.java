package com.zeno.viewinject.generate;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

/**
 * Created by Zeno on 2016/10/21.
 *
 * 抽象的辅助类生成器
 *
 * 用于定义生成模版类的规则 -- 模板方法
 *
 */
public abstract class AbstractGenerateAdapter implements IGenerateAdapter {

    private ProcessingEnvironment processingEnvironment;

    public AbstractGenerateAdapter(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    /**
     * 模版方法 ： 确定算法的框架 ，将算法的实现推迟到子类里面
     * @param map
     */
    @Override
    public void generate(Map<String, List<VariableElement>> map) {

    }

    /**
     * 生成import 头部
     * @param writer
     */
    protected abstract void generateImport(Writer writer,InjectInfo injectInfo);

    /**
     * 生成类字段
     * @param writer
     */
    protected abstract void generateField(Writer writer,VariableElement variableElement,InjectInfo injectInfo);

    /**
     * 生成尾部
     * @param writer
     */
    protected abstract void generateFooter(Writer writer);

    /**
     * 注入信息
     */
    class InjectInfo{

    }
}
