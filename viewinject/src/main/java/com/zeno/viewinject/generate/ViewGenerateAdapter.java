package com.zeno.viewinject.generate;

import java.io.Writer;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

/**
 * Created by Zeno on 2016/10/21.
 *
 * 生成View注解辅助类
 */
public class ViewGenerateAdapter extends AbstractGenerateAdapter {

    public ViewGenerateAdapter(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    protected void generateImport(Writer writer, InjectInfo injectInfo) {

    }

    @Override
    protected void generateField(Writer writer, VariableElement variableElement, InjectInfo injectInfo) {

    }

    @Override
    protected void generateFooter(Writer writer) {

    }
}
