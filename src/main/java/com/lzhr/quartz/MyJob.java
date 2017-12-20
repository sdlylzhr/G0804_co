package com.lzhr.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lizhongren1 on 2017/9/1.
 */
public class MyJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // 格式化输出日期(Date)
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(simpleDateFormat.format(new Date()));

        // 获取任务的具体信息
        // 获取任务名
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();

        // 获取传递的任务参数值
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String param = dataMap.getString("param");
        System.out.println("传递的参数是: " + param + "; 任务名是: " + jobName);
    }
}
