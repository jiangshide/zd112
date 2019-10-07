//package com.android.zdannotations_compile;
//
//import com.android.zdannotations.InjectView;
//import com.google.auto.service.AutoService;
//import com.squareup.javapoet.ClassName;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.MethodSpec;
//import com.squareup.javapoet.ParameterSpec;
//import com.squareup.javapoet.ParameterizedTypeName;
//import com.squareup.javapoet.TypeSpec;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.Filer;
//import javax.annotation.processing.Messager;
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.annotation.processing.Processor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.Modifier;
//import javax.lang.model.element.TypeElement;
//import javax.lang.model.util.Elements;
//import javax.lang.model.util.Types;
//import javax.tools.Diagnostic;
//
///**
// * created by jiangshide on 2019-08-16.
// * email:18311271399@163.com
// */
////@AutoService(Processor.class)
////@SupportedAnnotationTypes({Constants.BINDVIEW_ANNOTATION_TYPES})
////@SupportedSourceVersion(SourceVersion.RELEASE_7)
//
//
////用来生成META_INF/services/javax.annotation.processing.Processor文件
//@AutoService(Processor.class)
////允许/支持的注解类型,让注解处理器处理
//@SupportedAnnotationTypes({"com.android.zdannotations.InjectView"})
////指定JDK编译版本
//@SupportedSourceVersion(SourceVersion.RELEASE_7)
//public class ButterKnifeProcess extends AbstractProcessor {
//
//    private Elements elementUtils;
//
//    private Types typeUtils;
//
//    private Messager messager;
//
//    private Filer filer;
//
//    private Map<TypeElement, List<Element>> tempBindViewMap = new HashMap<>();
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnvironment) {
//        super.init(processingEnvironment);
//        //初始化
//        elementUtils = processingEnvironment.getElementUtils();
//        typeUtils = processingEnvironment.getTypeUtils();
//        messager = processingEnvironment.getMessager();
//        filer = processingEnvironment.getFiler();
//        messager.printMessage(Diagnostic.Kind.NOTE, "注解处理器初始化完成,开始处理注解......");
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
//        System.out.println("-------------jsd-set:" + set);
//        if (set != null) {
//            Set<? extends Element> bindViewElements = roundEnvironment.getElementsAnnotatedWith(InjectView.class);
//            if (bindViewElements != null) {
//                try {
//                    valueOfMap(bindViewElements);
//                    createJavaFile();
//                    return true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return false;
//    }
//
//    private void createJavaFile() throws IOException {
//        if (tempBindViewMap == null) return;
//        TypeElement viewBinderType = elementUtils.getTypeElement("com.android.zdannotations_compile.ViewBinder");
//        for (Map.Entry<TypeElement, List<Element>> entry : tempBindViewMap.entrySet()) {
//            ClassName className = ClassName.get(entry.getKey());
//            ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get(viewBinderType));
//
//            ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(entry.getKey()),
//                    "target"
//            ).build();
//
//            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
//                    .addAnnotation(Override.class)
//                    .addModifiers(Modifier.PUBLIC)
//                    .addParameter(parameterSpec);
//
//            for (Element fieldElement : entry.getValue()) {
//                String fieldName = fieldElement.getSimpleName().toString();
//                int annotationValue = fieldElement.getAnnotation(InjectView.class).value();
//                String methodContent = "$N." + fieldName + "$N.findViewById($L)";
//                methodBuilder.addStatement(methodContent, "target",
//                        "target",
//                        annotationValue
//                );
//            }
//
//
//            JavaFile.builder(className.packageName(),
//                    TypeSpec.classBuilder(className.simpleName() + "$$ViewBinder")
//                            .addSuperinterface(typeName)
//                            .addModifiers(Modifier.PUBLIC)
//                            .addMethod(methodBuilder.build())
//                            .build())
//                    .build()
//                    .writeTo(filer);
//        }
//    }
//
//    private void valueOfMap(Set<? extends Element> bindViewElements) {
//        if (bindViewElements != null) {
//            for (Element element : bindViewElements) {
//                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
//                if (tempBindViewMap.containsKey(enclosingElement)) {
//                    tempBindViewMap.get(enclosingElement).add(element);
//                } else {
//                    List<Element> fields = new ArrayList<>();
//                    fields.add(element);
//                    tempBindViewMap.put(enclosingElement, fields);
//                }
//            }
//        }
//    }
//}
