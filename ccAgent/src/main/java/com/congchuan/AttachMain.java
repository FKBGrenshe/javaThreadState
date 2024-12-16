package com.congchuan;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;


//import sun.jvmstat.monitor.MonitoredHost;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class AttachMain {

    public static final String QUERYSTRING = "请输入需要监控的进程ID:\n";
    public static final String STOPSTRING = "输入quit，停止线程\n请输入:";
    public static final String ILLEGALSTRING = "线程id为空，非法！,请重新输入:\n";

    public static final String STARTANDEND = "########################";

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {

        /*VirtualMachine vm = VirtualMachine.attach("17396");*/

        String threadId = getRunningJavaThreadId();

        // 获取当前进程的虚拟机对象
        VirtualMachine vm = VirtualMachine.attach(threadId);

        //找到了当前进程并且连接上了
        // 执行 java agent 里边的agentmain方法
        vm.loadAgent("D:\\Project\\javaProjects_github\\javaThreadState\\ccAgent\\target\\#maven-assembly-plugin\\ccAgent-1.0-SNAPSHOT-jar-with-dependencies.jar");
    }

    public static String getRunningJavaThreadId() throws IOException {

        // 获取进程列表，让用户手动进行输入
        // 1. 执行jps命令，打印所有进程列表
        Process jps = Runtime.getRuntime().exec("jps");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jps.getInputStream()));

        System.out.println(STARTANDEND);
        try{
            String line;
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(line);
            }
        }finally {
            if (bufferedReader != null){
                bufferedReader.close();
            }
        }
        System.out.println(STARTANDEND);
        System.out.println(QUERYSTRING + STOPSTRING);
        Scanner sc = new Scanner(System.in);
        String threadId = sc.nextLine();
        while (threadId.isEmpty()){
            System.out.println(ILLEGALSTRING + STOPSTRING);
            threadId = sc.nextLine();
        }
        if (threadId == "quit"){
            System.exit(0);
        }

        return threadId;
    }
}
