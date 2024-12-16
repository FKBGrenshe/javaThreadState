package com.congchuan.command;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadCommand {

    // 获取线程运行信息
    public static void printThreadInfo(){
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(
                threadMXBean.isObjectMonitorUsageSupported(),
                threadMXBean.isSynchronizerUsageSupported()
        );
        // 打印线程信息
        for (ThreadInfo threadInfo : threadInfos) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append("name:\t")
                    .append(threadInfo.getThreadName())
                    .append("threadId:\t")
                    .append(threadInfo.getThreadId())
                    .append("threadState:\t")
                    .append(threadInfo.getThreadState());
            System.out.println(stringBuilder);

            // 打印栈信息
            System.out.println("栈信息");
            StackTraceElement[] stackTrace = threadInfo.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                System.out.println(stackTraceElement);
            }

        }
    }

}
