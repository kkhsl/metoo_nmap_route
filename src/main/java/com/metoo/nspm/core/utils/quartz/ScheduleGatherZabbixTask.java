package com.metoo.nspm.core.utils.quartz;

import com.metoo.nspm.core.manager.admin.tools.DateTools;
import com.metoo.nspm.core.service.zabbix.IGatherService;
import com.metoo.nspm.core.service.zabbix.IProblemService;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @EnableScheduling：Spring系列框架中SpringFramwork自带的定时任务（org.springframework.scheduling.annotation.*）
 *
 * 注意事项：
 *  1：@Scheduled 多个任务使用了该方法，也是一个执行完执行下一个，并非并行执行；原因是scheduled默认线程数为1；
 *  2：每个定时任务才能执行下一次，禁止异步执行（@EnableAsync）
 *  3：并发执行，注意第“1”项里说明的，任务未执行完毕，另一个线程又来执行
 *
 */

@Configuration // 用于标记配置类，兼备Component
public class ScheduleGatherZabbixTask {

    Logger log = LoggerFactory.getLogger(ScheduleGatherZabbixTask.class);

    @Value("${task.switch.is-open}")
    private boolean flag;
    @Autowired
    private IGatherService gatherService;
    @Autowired
    private IProblemService problemService;

    static DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

    /**
     * 采集路由
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void updateRout(){
//        ThreadContext.bind(manager);
        if(flag) {
            Map params = new HashMap();
            Calendar calendar = Calendar.getInstance();
            params.put("endClock", DateTools.getTimesTamp10(calendar.getTime()));
            calendar.add(Calendar.MINUTE, -1);
            params.put("startClock", DateTools.getTimesTamp10(calendar.getTime()));
            int count = this.problemService.selectCount(params);
//            if(count > 0){
            if(true){
                Long time=System.currentTimeMillis();
                System.out.println("Rout开始采集");
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.clear(Calendar.SECOND);
                    cal.clear(Calendar.MILLISECOND);
                    this.gatherService.gatherRouteItem(cal.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("===Rout采集耗时：" + (System.currentTimeMillis()-time) + "===");
            }
        }
    }

}
