package com.metoo.nspm.core.manager.admin.action;

import com.metoo.nspm.core.service.zabbix.IGatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/admin/gather")
public class GatherManagerController {

    Logger log = LoggerFactory.getLogger(GatherManagerController.class);

    @Autowired
    private IGatherService gatherService;

    @RequestMapping("gatherRoute")
    public void gatherRoute(){
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        Date date = cal.getTime();
        try {
            this.gatherService.gatherRouteItem(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
