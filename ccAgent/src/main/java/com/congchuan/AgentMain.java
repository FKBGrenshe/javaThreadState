package com.congchuan;

import com.congchuan.command.ClassCommand;
import com.congchuan.command.MemoryCommand;
import com.congchuan.command.ThreadCommand;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.sql.SQLOutput;
import java.util.Scanner;

/**
 * @author fkbgr
 */
public class AgentMain {
    // premain
    public static void premain(String agentArgs, Instrumentation inst){
        System.out.println("premain doing...");
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws IOException {
        System.out.println("agentmain doing...");

        /**
         * 功能菜单
         */
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("菜单:\n" +
                    "1、查看内存使用情况\n"
                    + "2、生成堆内存快照\n"
                    + "3、打印栈信息\n"
                    + "4、打印类加载器\n"
                    + "5、打印类源码\n"
                    + "6、打印方法的参数和耗时\n"
                    + "7、退出\n"
            );
            String input = scanner.next();

            switch (input){
                case "1" : {
                    System.out.println("查看内存使用情况");
                    MemoryCommand.printMemory();
                    break;
                }
                case "2" : {
                    System.out.println("生成堆内存快照");
                    MemoryCommand.heapDump();
                    break;
                }
                case "3" : {
                    System.out.println("打印栈信息");
                    ThreadCommand.printThreadInfo();
                    break;
                }
                case "4" : {
                    System.out.println("打印类加载器");
                    ClassCommand.printAllClassLoader(inst);
                    break;
                }
                case "5" : {
                    System.out.println("打印类源码");
                    ClassCommand.printClassSourceCode(inst);
                    break;
                }
                case "6" : {
                    System.out.println("打印方法的参数和耗时 \n 使用 ASM技术增强源码 - byteBuddy");
                    ClassCommand.enhanceClassByteBuddy(inst);
                    break;
                }
                case "7" : {
                    return;
                }
            }
            /*System.out.println("ASM技术增强源码");
            ClassCommand.enhanceClass(inst);*/
        }




    }
}
