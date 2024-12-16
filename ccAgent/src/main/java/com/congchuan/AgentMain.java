package com.congchuan;

import com.congchuan.command.MemoryCommand;

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

        System.out.println("堆内存快照");
        MemoryCommand.heapDump();
    }
}
