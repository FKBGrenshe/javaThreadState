package com.congchuan.command;

import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.List;

public class MemoryCommand {
    // 打印所有内存信息
    public static void printMemory(){
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

        // 堆内存
        System.out.println("堆内存：");
        getMemoryInfo(memoryPoolMXBeans,MemoryType.HEAP);
        // 非堆内存
        System.out.println("非堆内存：");
        getMemoryInfo(memoryPoolMXBeans,MemoryType.NON_HEAP);
        // 打印nio相关内存
        System.out.println("nio相关内存");
        try {
            Class clazz = Class.forName("java.lang.management.BufferPoolMXBean");
            List<BufferPoolMXBean> bufferPoolMXBeans = ManagementFactory.getPlatformMXBeans(clazz);

            // 打印内容
            for (BufferPoolMXBean bufferPoolMXBean : bufferPoolMXBeans) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder
                        .append(" name:")
                        .append(bufferPoolMXBean.getName())
                        .append(bufferPoolMXBean.getMemoryUsed()/1024/1024)
                        .append("m")
                        //
                        .append(" capacity:")
                        .append(bufferPoolMXBean.getTotalCapacity()/1024/1024)
                        .append("m");
                System.out.println(stringBuilder);
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void getMemoryInfo(List<MemoryPoolMXBean> memoryPoolMXBeans, MemoryType type) {
        memoryPoolMXBeans.stream()
                .filter( x -> x.getType().equals(type))
                .forEach(x ->{
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder
                            .append(" name:")
                            .append(x.getName())
                            .append(x.getUsage().getUsed()/1024/1024)
                            .append("m")
                            //
                            .append(" committed:")
                            .append(x.getName())
                            .append(x.getUsage().getCommitted()/1024/1024)
                            .append("m")
                            //
                            .append(" max:")
                            .append(x.getName())
                            .append(x.getUsage().getMax()/1024/1024)
                            .append("m");
                    System.out.println(stringBuilder);
                });
    }
}
