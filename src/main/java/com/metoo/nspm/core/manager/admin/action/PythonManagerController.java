package com.metoo.nspm.core.manager.admin.action;

import com.metoo.nspm.core.service.nspm.INetworkElementService;
import com.metoo.nspm.core.utils.ResponseUtil;
import com.metoo.nspm.entity.nspm.NetworkElement;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PythonManagerController {

    @Autowired
    private INetworkElementService networkElementService;

    @GetMapping("/config/backup/{uuid}")
    public Object backupConfig(@PathVariable String uuid){
        if(Strings.isNotBlank(uuid)){
            NetworkElement ne = this.networkElementService.selectObjByUuid(uuid);
            if(ne != null){

            }
        }
        return ResponseUtil.badArgument();
    }
}
