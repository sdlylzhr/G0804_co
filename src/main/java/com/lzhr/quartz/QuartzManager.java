package com.lzhr.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by lizhongren1 on 2017/8/31.
 */
public class QuartzManager {

    // 定时器工厂
    private static SchedulerFactory factory = new StdSchedulerFactory();

    // 设定 任务组(job group) 的名字(随便设定)
    private static String JOB_GROUP_NAME = "LZHR_JOBGROUP_NAME";
    // 设定 触发器组(trigger group) 的名字(随便设定)
    private static String TRIGGER_GROUP_NAME = "LZHR_TRIGGERGROUP_NAME";


    // 添加一个job
    public static void addJob(String jobName, Class<? extends Job> cls, String time){

        try {
            Scheduler scheduler = factory.getScheduler();
            // 添加一个定时任务
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName,JOB_GROUP_NAME).build();

            // 给任务添加参数, 可以在任务内部获取到.
            jobDetail.getJobDataMap().put("param","2222");

            // 触发器(2种方式)
            // 通过时间表达式创建触发器(具体时间表达式可以看最下方注释)
//              time = "0/2 * * * * ?";
              Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)
                     .withSchedule(CronScheduleBuilder.cronSchedule(time)).build();

            // 普通触发器, 不设定具体时间, 从start开始按设定时间间隔计算.
//            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", TRIGGER_GROUP_NAME)
//                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(Integer.valueOf(time)).repeatForever()).build();

            // 添加到scheduler中
            scheduler.scheduleJob(jobDetail,trigger);

            // 如果scheduler没有关闭,就启动
            if (!scheduler.isShutdown()){
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    // 修改任务的触发时间(使用默认的任务组名,触发器名,触发器组名)
    public static void modifyJobTime(String jobname, String time){
        try {
            Scheduler scheduler = factory.getScheduler();

            // 根据trigger的name和触发器组名获取
            // 如果创建触发器的时候是使用SimpleTrigger, 那么这里要强转为SimpleTrigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(new TriggerKey(jobname,TRIGGER_GROUP_NAME));

            System.out.println(trigger);

            if (trigger == null){
                return;
            }

            // 获取之前的时间表达式
            String oldTime = trigger.getCronExpression();

            // 如果前后不一致, 就移除前面的任务, 添加新的
            if (!oldTime.equalsIgnoreCase(time)){
                // 获取任务信息
                JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobname,JOB_GROUP_NAME));
                // 获取任务的class信息
                Class jobJobClass = jobDetail.getJobClass();

                // 移除之前的任务
                removeJob(jobname);
                // 添加新任务
                addJob(jobname, jobJobClass, time);

            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    // 移除任务
    public static void removeJob(String jobname){

        try {
            Scheduler scheduler = factory.getScheduler();
            // 先暂停scheduler
            scheduler.pauseTrigger(new TriggerKey(jobname, TRIGGER_GROUP_NAME));
            // 然后将任务从scheduler任务序列中移除
            scheduler.unscheduleJob(new TriggerKey(jobname, TRIGGER_GROUP_NAME));
            // 最后删除任务
            scheduler.deleteJob(new JobKey(jobname, JOB_GROUP_NAME));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    // 启动所有定时任务
    public static void startJobs(){
        try {
            Scheduler scheduler = factory.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    // 关闭所有定时任务
    public static void shutdownJobs(){
        try {
            Scheduler scheduler = factory.getScheduler();
            if (! scheduler.isShutdown()){
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }


}

/*
 时间表达式:
 时间格式: s m h d m w(?) y(?),   分别对应: 秒s 分m 时h 日d 月m 周w 年y
 例如: 2 3 4 5 6 ?
 意思是 每个6月5日4时3分2秒 开始执行
 或者如: 10 3 * * * ?
 意思是 每小时的3分10秒 开始执行
 斜杠(/)的意思是固定步长  开始值/增量步长值
 比如下面这个 0/2 * * * * ?
 代表 每分钟从0秒开始,每隔2秒执行.
*/