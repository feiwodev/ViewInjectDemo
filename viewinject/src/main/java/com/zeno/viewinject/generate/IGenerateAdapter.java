package com.zeno.viewinject.generate;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.VariableElement;

/**
 * Created by Zeno on 2016/10/21.
 *
 * 辅助类生成器
 */
public interface IGenerateAdapter {

    /**
     * 根据解析注解的内容生成辅助类
     * @param map
     */
    void generate(Map<String,List<VariableElement>> map);
}
