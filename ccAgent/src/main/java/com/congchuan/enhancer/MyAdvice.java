package com.congchuan.enhancer;


import net.bytebuddy.asm.Advice;

public class MyAdvice {

    // 方法进入时，打印所有参数，返回开始时间
    @Advice.OnMethodEnter
    static long enter(@Advice.AllArguments Object[] ary){
        if(ary != null){
            for (int i = 0; i < ary.length; i++) {
                System.out.println("arg " + i + ":\t" + ary[i]);
            }
        }
        // 返回纳秒值，开始时间
        return System.nanoTime();
    }

    // 方法退出的时候，统计方法耗时
    @Advice.OnMethodExit
    static void exit(@Advice.Enter long value){
        System.out.println("executing time = " + (System.nanoTime() - value) + "ns" );
    }

}
