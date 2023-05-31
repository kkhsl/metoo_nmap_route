package com.metoo.nspm.core.manager.admin.action;

import com.metoo.nspm.core.service.nspm.IConfigBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/backup")
public class ConfigBackupManagerController {

    @Autowired
    private IConfigBackupService configBackupService;

    @GetMapping("/config")
    public Object getInstance(){
        return configBackupService.getInstance();
    }
}
