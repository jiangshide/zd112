package com.android.zdannotations_compile;

import com.android.zdannotations.BindPath;
import com.android.zdannotations.InjectView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

//@AutoService(Processor.class)
//用来生成META_INF/services/javax.annotation.processing.Processor文件
@AutoService(Processor.class)
//允许/支持的注解类型,让注解处理器处理
//@SupportedAnnotationTypes({BindPath.class.getCanonicalName()})
//指定JDK编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ZdAnnotationsProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Map<String, String> map = new HashMap<>();

    //操作Element工具类(类,函数,属性都是Element)
    private Elements elementUtils;

    //Messager用来报告错误,警告和其它提示信息
    private Messager messager;

//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return processingEnv.getSourceVersion();
//    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.mFiler = processingEnvironment.getFiler();
        this.elementUtils = processingEnvironment.getElementUtils();
        this.messager = processingEnvironment.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(BindPath.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("-----set" + set);
//        if (!set.isEmpty()) {
//            //获取所有被@Helloworld注解的元素集合
//            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
//            if (!elements.isEmpty()) {
//                for (Element element : elements) {
//                    parseElemets(element);
//                }
//            }
//
//            Set<? extends Element> bindViewElements = roundEnvironment.getElementsAnnotatedWith(InjectView.class);
//            if (!bindViewElements.isEmpty()) {
//                valueOfMap(bindViewElements);
//                createJavaFile();
//                return true;
//            }
//        }
//        return false;

//        if (set.isEmpty()) return false;
//        //获取所有的带ARouter注解的类节点
//        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
//        for (Element element : elements) {
//            //通过类节点获取包节点
//            String pkgName = elementUtils.getPackageOf(element).getQualifiedName().toString();
//            //获取简单类名
//            String className = element.getSimpleName().toString();
//            messager.printMessage(Diagnostic.Kind.NOTE, "被注解的类有:" + className);
//            //最终想生成的类文件
//            String finalClassName = className + "$$Router";
//            //公开课写法,也是EventBus写法:https://github.com/greenrobot/EventBus
//            try {
//                //创建一个新的源文件class并返回一个对象以允许写入它
//                JavaFileObject sourceFile = mFiler.createSourceFile(pkgName + '.' + finalClassName);
//                //定义Writer对象，开启写入
//                Writer writer = sourceFile.openWriter();
//                //设置包名
//                writer.write("package " + pkgName + ";\n");
//                writer.write("public class " + finalClassName + " {\n");
//                writer.write("public void putActivity(String path){\n}\n}");
//                //最后结束
//                writer.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return true;

        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        //初始化数据
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            String key = typeElement.getAnnotation(BindPath.class).value();
            String value = typeElement.getQualifiedName().toString();
            System.out.println("------key:" + key + " | value:" + value);
            Writer writer = null;
            //创建类名
            String utilName = "ActivityUtil" + System.nanoTime();
            //创建文件
            try {
                JavaFileObject sourceFile = mFiler.createSourceFile("com.android.router." + utilName);
                writer = sourceFile.openWriter();
                writer.write("package com.android.router;\n" +
                        "\n" +
                        "import com.android.zdrouter.IZdRouter;\n" +
                        "import com.android.zdrouter.ZdRouter;\n" +
                        "\n" +
                        "public class " + utilName + " implements IZdRouter {\n" +
                        "    @Override\n" +
                        "    public void putActivity() {\n");
                writer.write("\t\tZdRouter.getInstance().putActivity(\"" + key + "\"," + value + ".class);");
                writer.write("\n\t}\n}");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    private void parseElemets(Element element) {
        String pkgName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        String className = element.getSimpleName().toString();
        String finalClassName = className + "$$Router";


        ClassName superinterface = ClassName.bestGuess("com.android.zdrouter.IZdRouter");

        MethodSpec putActvitiy = MethodSpec.methodBuilder("putActivity")
                .addAnnotation(Override.class)
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC)
//                .addStatement("ZdRouter.getInstance().putActivity(\"splash/splash\",com.android.zd112."+className+".class);")
                .build();

        TypeSpec routerClass = TypeSpec.classBuilder(finalClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(superinterface)
                .addMethod(putActvitiy)
                .build();


        //方法体
//        MethodSpec main = MethodSpec.methodBuilder("put")//方法名
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)//方法修饰符
//                .returns(void.class)//方法返回值(默认void)
//                .addParameter(String[].class, "args")//方法参数
////                .addStatement("$T.out.println($s)", System.class, "Hello,JavaPoet!")//方法内容
//                .build();//构建
//
//
//        //类
//        TypeSpec helloWorld = TypeSpec.classBuilder(finalClassName)//类名
//                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)//类修饰符
//                .addSuperinterface(superinterface)
//                .addMethod(main)//加入方法
//                .build();//构建
//
//        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("putActivity")
//                .addAnnotation(Override.class)
//                .returns(TypeName.VOID);


        //文件生成器(生成在指定=包名+类)
        JavaFile javaFile = JavaFile.builder(pkgName, routerClass).build();

        //写文件
        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<TypeElement, List<Element>> tempBindViewMap = new HashMap<>();

    private void createJavaFile() {
        if (tempBindViewMap.isEmpty()) return;
        TypeElement viewBinderType = elementUtils.getTypeElement("com.android.base.ViewBinder");
        for (Map.Entry<TypeElement, List<Element>> entry : tempBindViewMap.entrySet()) {
            ClassName className = ClassName.get(entry.getKey());
            ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get(viewBinderType));

            ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(entry.getKey()),
                    "target"
            ).build();

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(parameterSpec);

            for (Element fieldElement : entry.getValue()) {
                String fieldName = fieldElement.getSimpleName().toString();
                int annotationValue = fieldElement.getAnnotation(InjectView.class).value();
                String methodContent = "$N." + fieldName + "$N.findViewById($L)";
                methodBuilder.addStatement(methodContent, "target",
                        "target",
                        annotationValue
                );
            }


            try {
                JavaFile.builder(className.packageName(),
                        TypeSpec.classBuilder(className.simpleName() + "$$ViewBinder")
                                .addSuperinterface(typeName)
                                .addModifiers(Modifier.PUBLIC)
                                .addMethod(methodBuilder.build())
                                .build())
                        .build()
                        .writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void valueOfMap(Set<? extends Element> bindViewElements) {
        if (!bindViewElements.isEmpty()) {
            for (Element element : bindViewElements) {
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                if (tempBindViewMap.containsKey(enclosingElement)) {
                    tempBindViewMap.get(enclosingElement).add(element);
                } else {
                    List<Element> fields = new ArrayList<>();
                    fields.add(element);
                    tempBindViewMap.put(enclosingElement, fields);
                }
            }
        }
    }
}
