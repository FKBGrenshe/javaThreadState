package com.congchuan.command;

import com.congchuan.enhancer.AsmEnhancer;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;
import org.jd.core.v1.api.printer.Printer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ClassCommand {

    // 打印所有类加载器
    public static void printAllClassLoader(Instrumentation inst){
        HashSet<ClassLoader> classLoaderSet = new HashSet<>();
        // 获取所有的类
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        for (Class allLoadedClass : allLoadedClasses) {
            ClassLoader classLoader = allLoadedClass.getClassLoader();
            classLoaderSet.add(classLoader);
        }

        // 打印类加载器
        String string = classLoaderSet
                .stream()
                .map(x -> x == null ? "BootStrapClassLoader" : x.toString())
                .distinct()
                .sorted((s1, s2) -> s1.compareTo(s2))
                .collect(Collectors.joining(",\t"));

        System.out.println(string);
    }
    // 打印类的源代码
    public static void printClassSourceCode(Instrumentation inst){
        // 让用户输入类名
        System.out.println("去输入需要打印源码的类名：");
        Scanner scanner = new Scanner(System.in);
        String className = scanner.next();
        while (className.isEmpty()){
            System.out.println("类名不能为空！请重新输入");
        }
        // 根据类名找到class对象
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        for (Class allLoadedClass : allLoadedClasses) {
            if (allLoadedClass.getName().equals(className)){
                System.out.println("已找到："+allLoadedClass);

                // 创建 类文件转换器 -- 不修改版
                ClassFileTransformer classFileTransformer = new ClassFileTransformer() {
                    @Override
                    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                        // 打印字节码信息
                        System.out.println("字节码信息：\t" + classfileBuffer);
                        try {
                            // 通过JDCore反编译源代码
                            printJDCoreSourceCode(classfileBuffer,className);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 返回null不产生任何转换
                        return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                    }
                };

                // 添加转换器
                inst.addTransformer(classFileTransformer,true);

                // 2. 手动触发转换
                try {
                    inst.retransformClasses(allLoadedClass);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }finally {
                    // 3. 删除转换器
                    inst.removeTransformer(classFileTransformer);
                }
            }
        }
    }
    // 对类进行增强，统计执行耗时
    public static void enhanceClass(Instrumentation inst){
        // 让用户输入类名
        System.out.println("去输入需要打印源码的类名：");
        Scanner scanner = new Scanner(System.in);
        String className = scanner.next();
        while (className.isEmpty()){
            System.out.println("类名不能为空！请重新输入");
        }
        // 根据类名找到class对象
        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        for (Class allLoadedClass : allLoadedClasses) {
            if (allLoadedClass.getName().equals(className)){
                System.out.println("已找到："+allLoadedClass);
                // 创建 类文件转换器 -- 不修改版
                ClassFileTransformer classFileTransformer = new ClassFileTransformer() {
                    @Override
                    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                        // 通过ASM对类进行增强，返回字节码信息
                        byte[] bytes = AsmEnhancer.enhanceClass(classfileBuffer);
                        return bytes;
                        /*// 打印字节码信息
                        System.out.println("字节码信息：\t" + classfileBuffer);
                        try {
                            // 通过JDCore反编译源代码
                            printJDCoreSourceCode(classfileBuffer,className);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 返回null不产生任何转换
                        return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);*/
                    }
                };

                // 添加转换器
                inst.addTransformer(classFileTransformer,true);

                // 2. 手动触发转换
                try {
                    inst.retransformClasses(allLoadedClass);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }finally {
                    // 3. 删除转换器
                    inst.removeTransformer(classFileTransformer);
                }
            }
        }
    }


    // 通过jd-core来打印源代码
    private static void printJDCoreSourceCode(byte[] bytes, String className) throws Exception {
        // loader
        Loader loader = new Loader() {
            @Override
            public byte[] load(String internalName) throws LoaderException {
                return bytes;
            }

            @Override
            public boolean canLoad(String internalName) {
                return true;
            }
        };

        // printer
        Printer printer = new Printer() {
            protected static final String TAB = "  ";
            protected static final String NEWLINE = "\n";

            protected int indentationCount = 0;
            protected StringBuilder sb = new StringBuilder();

            @Override public String toString() { return sb.toString(); }

            @Override public void start(int maxLineNumber, int majorVersion, int minorVersion) {}
            @Override public void end() {
                // 打印源代码
                System.out.println(sb);
            }

            @Override public void printText(String text) { sb.append(text); }
            @Override public void printNumericConstant(String constant) { sb.append(constant); }
            @Override public void printStringConstant(String constant, String ownerInternalName) { sb.append(constant); }
            @Override public void printKeyword(String keyword) { sb.append(keyword); }
            @Override public void printDeclaration(int type, String internalTypeName, String name, String descriptor) { sb.append(name); }
            @Override public void printReference(int type, String internalTypeName, String name, String descriptor, String ownerInternalName) { sb.append(name); }

            @Override public void indent() { this.indentationCount++; }
            @Override public void unindent() { this.indentationCount--; }

            @Override public void startLine(int lineNumber) {
                sb.append(TAB.repeat(Math.max(0, indentationCount)));
            }
            @Override public void endLine() { sb.append(NEWLINE); }
            @Override public void extraLine(int count) { while (count-- > 0) {
                sb.append(NEWLINE);
            }
            }

            @Override public void startMarker(int type) {}
            @Override public void endMarker(int type) {}
        };

        // 通过jdcore打印
        ClassFileToJavaSourceDecompiler decompiler = new ClassFileToJavaSourceDecompiler();

        decompiler.decompile(loader, printer, className);


    }

}
