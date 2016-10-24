# ViewInjectDemo
ViewInjectDemo

ButterKinfe 框架解析实验


###前言
Butterknife我相信，对大部分做Android开发的人都不陌生，这个是供职于Square公司的JakeWharton大神开发的，目前github的star为 ~12449~ 。使用这个库，在AS中搭配Android ButterKnife Zelezny插件，简直是开发神器，从此摆脱繁琐的`findViewById(int id)`，也不用自己手动`@bind(int id)` ， 直接用插件生成即可。这种采用注解DI组件的方式，在Spring中很常见，起初也是在Spring中兴起的 。今天我们就一探究竟，自己实现一个butterknife `（有不会用的，请自行Google）`。

> 项目地址： [JakeWharton/butterknife](https://github.com/JakeWharton/butterknife)

![butterknife](http://upload-images.jianshu.io/upload_images/643851-b2f6b913068acb4d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###实现原理 `（假定你对注解有一定的了解）`
> 注解

对ButterKnife有过了解人 ， 注入字段的方式是使用注解`@Bind(R.id.tv_account_name)`，但首先我们需要在`Activity`声明注入`ButterKnife.bind(Activity activity)` 。我们知道，注解分为好几类， 有在源码生效的注解，有在类文件生成时生效的注解，有在运行时生效的注解。分别为`RetentionPolicy.SOURCE`，`RetentionPolicy.CLASS`，`RetentionPolicy.RUNTIME` ，其中以`RetentionPolicy.RUNTIME`最为消耗性能。而ButterKnife使用的则是编译器时期注入，在使用的时候，需要配置`classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'` ， 这个配置说明，在编译的时候，进行注解处理。要对注解进行处理，则需要继承`AbstractProcessor` ， 在`boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)`中进行注解处理。

> 实现方式

知晓了注解可以在编译的时候进行处理，那么，我们就可以得到注解的字段属性与所在类 ， 进而生成注入文件，生成一个注入类的内部类，再进行字段处理 ， 编译之后就会合并到注入类中，达到植入新代码段的目的。例如：我们注入`@VInjector(R.id.tv_show) TextView tvShow;`我们就可以得到`tvShow`这个变量与`R.id.tv_show`这个id的值，然后进行模式化处理`injectObject.tvShow = injectObject.findViewById(R.id.tv_show);` ，再将代码以内部类的心事加入到组件所在的类中 ， 完成一次DI（注入） 。

> 实现流程图

![view_injector_流程图](http://upload-images.jianshu.io/upload_images/643851-993ed4d1d5cae1d3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

① 首先创建一个视图注解
② 创建一个注解处理器，用来得到注解的属性与所属类
③ 解析注解，分离组合Class与属性
④ 组合Class与属性，生成新的Java File

### 项目UML图

![ViewInject UML](http://upload-images.jianshu.io/upload_images/643851-aa698ef3cc292124.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

> 简要说明：

主要类：
`VInjectProcessor`  ---->  注解处理器 ， 需要配置注解处理器
```
resources
        - META-INF
              - services
                    - javax.annotation.processing.Processor
```
Processor内容：
```
com.zeno.viewinject.apt.VInjectProcessor   # 指定处理器全类名
```
图示：

![processor config](http://upload-images.jianshu.io/upload_images/643851-7ebad29590ffb330.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

`VInjectHandler`  ----> 注解处理类 ， 主要进行注入类与注解字段进行解析与封装，将同类的字段使用map集合进行映射。exp: Map<Class,List<Attr>> 。

 `ViewGenerateAdapter`  -----> Java File 生成器，将注入的类与属性，重新生成一个Java File，是其注入类的内部类 。


### 具体实现

> 一 ， 创建注解 , 对视图进行注解，R.id.xxx ， 所以注解类型是int类型

```java 
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
```

> 二， 注解处理器  `关于注解处理器配置，上面已经做了说明`

```java
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
        // init annotation handler , add handler
        registerHandler(new VInjectHandler());

        // init generate adapter
        mGenerateAdapter = new ViewGenerateAdapter(processingEnv);

    }

    /*可以有多个处理*/
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
        mGenerateAdapter.generate(mHandleAnnotationMap);
        // 表示处理
        return true;
    }
}
```

> 对得到的注解进行处理  ， 主要是进行注解类型与属性进行分离合并处理，因为一个类有多个属性，所以采用map集合，进行存储，数据结构为：Map<String:className , List<VariableElement:element>>

```java
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
```

> 生成Java File ， 根据获取的属性与类，创建一个注入类的内部类

```java
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
    protected void generateImport(Writer writer, InjectInfo injectInfo) throws IOException {
        writer.write("package "+injectInfo.packageName+";");
        writer.write("\n\n");
        writer.write("import  com.zeno.viewinject.adapter.IVInjectorAdapter;");
        writer.write("\n\n");
        writer.write("import  com.zeno.viewinject.utils.ViewFinder;");
        writer.write("\n\n\n");
        writer.write("/* This class file is generated by ViewInject , do not modify */");
        writer.write("\n");
        writer.write("public class "+injectInfo.newClassName+" implements IVInjectorAdapter<"+injectInfo.className+"> {");
        writer.write("\n\n");
        writer.write("public void injects("+injectInfo.className+" target) {");
        writer.write("\n");
    }

    @Override
    protected void generateField(Writer writer, VariableElement variableElement, InjectInfo injectInfo) throws IOException {
        VInjector vInjector = variableElement.getAnnotation(VInjector.class);
        int resId = vInjector.value();
        String fieldName = variableElement.getSimpleName().toString();
        writer.write("\t\ttarget."+fieldName+" = ViewFinder.findViewById(target,"+resId+");");
        writer.write("\n");
    }

    @Override
    protected void generateFooter(Writer writer) throws IOException {
        writer.write(" \t}");
        writer.write("\n\n");
        writer.write("}");
    }
}
```

### 结语
ButterKnife类型的注解框架，其主要核心就是编译时期注入， 如果是采用运行时注解的话，那性能肯定影响很大，国内有些DI框架就是采用的运行时注解，所以性能上会有所损伤 。原以为很高深的东西，其实剖析过原理之后，也就渐渐明白了，不再视其为高深莫测，我们自己也可以实现同等的功能。

程序员最好的学习方式就是，学习别人的代码，特别是像jakeWharton这样的大神的代码，值得研究与学习 ， 然后模仿之。

### 源码
[ViewInjectDemo](https://github.com/zhuyongit/ViewInjectDemo)  UML图与流程图都会放在github上