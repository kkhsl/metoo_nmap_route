package com.metoo.nspm.core.config.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobTest {

    Logger log = LoggerFactory.getLogger(JobTest.class);

//    @Scheduled(cron = "*/3 * * * * ?")
//    public void w() throws InterruptedException {
//        Long time=System.currentTimeMillis();
//        System.out.println(Thread.currentThread().getId() + "任1务开始");
//        System.out.println("===任务执行耗时："+(System.currentTimeMillis()-time)+"===");
//
//    }
//
//    @Scheduled(cron = "*/3 * * * * ?")
//    public void ee() throws InterruptedException {
//        Long time=System.currentTimeMillis();
//        Thread.sleep(10000);
//        System.out.println(Thread.currentThread().getId() + "任2务开始");
//        System.out.println("===任务执行耗时："+(System.currentTimeMillis()-time)+"===");
//
//    }
//
//    @Scheduled(cron = "*/3 * * * * ?")
//    public void eee() throws InterruptedException {
//        Long time=System.currentTimeMillis();
//        System.out.println(Thread.currentThread().getId() + "任3务开始");
//        System.out.println("===任务执行耗时："+(System.currentTimeMillis()-time)+"===");
//    }

//    @Scheduled(cron = "*/3 * * * * ?")
//    public void e() throws InterruptedException {
//        log.info(Thread.currentThread().getId() + "任1务开始");
//    }
//
//    @Scheduled(cron = "*/3 * * * * ?")
//    public void ee() throws InterruptedException {
//
//        Thread.sleep(10000);
//        log.info(Thread.currentThread().getId() + "任2务开始");
//    }
//
//    @Scheduled(cron = "*/3 * * * * ?")
//    public void eee() throws InterruptedException {
//        log.info(Thread.currentThread().getId() + "任3务开始");
//    }
}
