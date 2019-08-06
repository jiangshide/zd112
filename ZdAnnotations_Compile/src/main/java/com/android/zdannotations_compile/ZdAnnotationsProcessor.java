package com.android.zdannotations_compile;

import com.android.zdannotations.BindPath;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class ZdAnnotationsProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Map<String, String> map = new HashMap<>();

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.mFiler = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(BindPath.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        //初始化数据
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            String key = typeElement.getAnnotation(BindPath.class).value();
            String value = typeElement.getQualifiedName().toString();
            System.out.println("------key:" + key + " | value:" + value);
            map.put(key, value);

            Writer writer = null;
            //创建类名
            String utilName = "ActivityUtil" + System.nanoTime();
//            String utilName = value+System.currentTimeMillis();
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
}
