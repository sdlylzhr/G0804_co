package com.lzhr.test;

import com.lzhr.quartz.MyJob;
import com.lzhr.quartz.QuartzManager;
import org.junit.Test;

/**
 * Created by lizhongren1 on 2017/8/31.
 */
public class MainTest {

    @Test
    public void mainTest(){

        try {

            String job_name = "动态任务调度";
            System.out.println("----任务启动: 开始(10秒一次)----");
            QuartzManager.addJob(job_name, MyJob.class, "0/10 * * * * ?");

            Thread.sleep(2000);

            System.out.println("====修改时间: 开始(2秒一次)====");
            QuartzManager.modifyJobTime(job_name, "0/2 * * * * ?");

            Thread.sleep(6000);

            System.out.println("~~~~移除定时: 开始~~~~");
            QuartzManager.removeJob(job_name);
            System.out.println("****移除成功****");

            System.out.println("----再次添加任务: 开始(10秒一次)----");
            QuartzManager.addJob(job_name, MyJob.class, "0/10 * * * * ?");
            Thread.sleep(60000);

            System.out.println("~~~~移除定时: 开始~~~~");
            QuartzManager.removeJob(job_name);
            System.out.println("****移除成功****");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
