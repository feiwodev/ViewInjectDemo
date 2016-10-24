package com.zeno.viewinject.generate;

import com.zeno.viewinject.ViewInject;
import com.zeno.viewinject.utils.AnnotationUtils;
import com.zeno.viewinject.utils.PrintUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

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
    private final Filer mFiler;

    public AbstractGenerateAdapter(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
        mFiler = processingEnvironment.getFiler();
    }

    /**
     * 模版方法 ： 确定算法的框架 ，将算法的实现推迟到子类里面
     * @param map
     */
    @Override
    public void generate(Map<String, List<VariableElement>> map) {
        Set<Map.Entry<String, List<VariableElement>>> entries = map.entrySet();
        Iterator<Map.Entry<String, List<VariableElement>>> iterator = entries.iterator();
        PrintUtils.print("size === "+entries.size());
        // 遍历出注入的属性集合
        while (iterator.hasNext()){
            Map.Entry<String, List<VariableElement>> entry = iterator.next();
            List<VariableElement> elementList = entry.getValue();
            if (elementList == null || elementList.isEmpty()) {
                continue;
            }

            InjectInfo injectInfo = createInjectInfo(elementList.get(0));
            /*创建一个Java源文件*/
            Writer writer = null;
            try {
                JavaFileObject javaFileObject = mFiler.createSourceFile(injectInfo.getFullClassName());
                writer =  javaFileObject.openWriter();
                /*头部*/
                generateImport(writer,injectInfo);
                /*属性部分*/
                for(VariableElement element : elementList) {
                    generateField(writer,element,injectInfo);
                }
                PrintUtils.print("\n\n what is one ?....\n\n");
                /*尾部*/
                generateFooter(writer);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 创建一个InjectInfo对象
     * @param variableElement
     * @return
     */
    private InjectInfo createInjectInfo(VariableElement variableElement) {
        TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
        String className = typeElement.getSimpleName().toString();
        String packageName = AnnotationUtils.getPackageName(processingEnvironment, typeElement);
        return new InjectInfo(packageName,className);
    }


    /**
     * 生成import 头部
     * @param writer
     */
    protected abstract void generateImport(Writer writer,InjectInfo injectInfo) throws IOException;

    /**
     * 生成类字段
     * @param writer
     */
    protected abstract void generateField(Writer writer,VariableElement variableElement,InjectInfo injectInfo) throws IOException;

    /**
     * 生成尾部
     * @param writer
     */
    protected abstract void generateFooter(Writer writer) throws IOException;

    /**
     * 注入信息
     */
    class InjectInfo{
        public String packageName;
        public String className;
        public String newClassName;

        public InjectInfo(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
            this.newClassName = className+ ViewInject.SUFFIX;
        }

        /**
         * 辅助类的完整类名
         * @return string className
         */
        public String getFullClassName(){
            return packageName+"."+newClassName;
        }

    }
}
