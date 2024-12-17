package com.congchuan;

import com.congchuan.command.ClassCommand;
import com.congchuan.command.MemoryCommand;
import com.congchuan.command.ThreadCommand;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class AgentMain {
    // premain
    public static void premain(String agentArgs, Instrumentation inst){
        System.out.println("premain doing...");
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws IOException {
        System.out.println("agentmain doing...");
        System.out.println("打印内存状态ing");
        MemoryCommand.printMemory();

        /*System.out.println("堆内存快照");
        MemoryCommand.heapDump();*/

        System.out.println("线程信息");
        ThreadCommand.printThreadInfo();

        System.out.println("类加载器信息");
        ClassCommand.printAllClassLoader(inst);

        System.out.println("类源码");
        ClassCommand.printClassSourceCode(inst);

        System.out.println("ASM技术增强源码");
        ClassCommand.enhanceClass(inst);



    }
}
