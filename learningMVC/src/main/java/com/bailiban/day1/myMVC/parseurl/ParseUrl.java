package com.bailiban.day1.myMVC.parseurl;

import com.bailiban.day1.myMVC.model.MethodInfo;
import com.bailiban.day1.myMVC.myannotation.MyRequestMapping;
import com.bailiban.day1.myMVC.myannotation.MyRestController;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ParseUrl {
    private static Map<String,Object> beanMap = new HashMap<>();
    private static Map<String, MethodInfo> methodMap = new HashMap<>();

    // 1. 初始化 beanMap
    // 2. 初始化 methodMap
    public static void refreshBeanFactory(String pkg) {
        URL url = ParseUrl.class.getClassLoader().getResource(pkg.replace(".","/"));
        File file = new File(url.getPath());
        parseFile(file);
        //System.out.println(file);
       /* for (Map.Entry entry:beanMap.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }*/
    }
    //解析file
    private static void parseFile(File file) {
        if (!file.isDirectory()){
            return;
        }
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathName) {
                if (pathName.isDirectory()){
                    parseFile(pathName);
                    return false;
                }
                return pathName.getName().endsWith(".class");
            }
        });
        for (File f:files) {
            String classPath = f.getAbsolutePath().split("classes\\\\")[1].replace("\\", ".").replace(".class", "");
            try {
                Class<?> aClass = Class.forName(classPath);
                if (aClass.getDeclaredAnnotation(MyRestController.class)!=null){
                    parseClass(aClass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void parseClass(Class<?> aClass) {
        try {
            beanMap.put(aClass.getSimpleName(),aClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method:methods) {
            MyRequestMapping myRequestMapping = method.getDeclaredAnnotation(MyRequestMapping.class);
            if (myRequestMapping!=null){
                String url = myRequestMapping.value();
                methodMap.put(url,new MethodInfo(method,aClass.getSimpleName()));
            }
        }
    }
    //解析url

    /**
     *
     * @param url 全路径
     */
    public static String parseUrl(String url) throws InvocationTargetException, IllegalAccessException {
        if (url.contains(".ico")){
            return "";
        }
        HashMap<String,String> urlParameter = new HashMap<String,String>();
        if (!url.contains("?")){
            return methodInvoke(url,urlParameter);
        }
        String[] split = url.split("\\?");
        String[] parameters = url.replaceFirst(".*?\\?","").split("&");
        url = split[0];
        for (String parameter:parameters) {
            if (!parameter.contains("=")){
                continue;
            }
            String[] split1 = parameter.split("=");
            urlParameter.put(split1[0],split1[1]);
        }

        return methodInvoke(url,urlParameter);

    }
    //调用方法

    /**
     *
     * @param url 不带参数的路径
     * @param urlParameter 路径带的参数（参数名，value）
     * @return
     */
    public static String methodInvoke(String url,HashMap<String,String> urlParameter) throws InvocationTargetException, IllegalAccessException {
        //根据URl从容器取出method和对象
        MethodInfo methodInfo = methodMap.get(url);
        if (methodInfo==null){
            return "404";
        }
        String className = methodInfo.getClassName();
        Object o = beanMap.get(className);
        Method method = methodInfo.getMethod();
        //判断有无参数
       /* if (urlParameter.size()==0){
            return (String) method.invoke(o);
        }*/
        //接收参数的数组
        Object[] parameters = new Object[urlParameter.size()];
        Parameter[] methodParameters = method.getParameters();
        if(parameters.length!=methodParameters.length){
            return "参数个数不匹配";
        }
        //判断参数类型
        for (int i = 0; i<methodParameters.length;i++){
            String name = methodParameters[i].getName();
            String type = methodParameters[i].getType().getSimpleName();
            boolean flag = false;
            if(type.equals("int")){
                parameters[i] = Integer.parseInt(urlParameter.get(name));
                flag = true;
            }else if (type.equals("String")){
                parameters[i] = urlParameter.get(name);
                flag = true;
            }
            if (!flag){
                return "404";
            }
        }
        return (String) method.invoke(o, parameters);
    }

}
