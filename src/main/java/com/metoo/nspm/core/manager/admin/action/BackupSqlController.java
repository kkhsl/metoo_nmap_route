package com.metoo.nspm.core.manager.admin.action;

import com.github.pagehelper.Page;
import com.metoo.nspm.core.service.nspm.IBackupSqlService;
import com.metoo.nspm.core.utils.Global;
import com.metoo.nspm.core.utils.MyStringUtils;
import com.metoo.nspm.core.utils.ResponseUtil;
import com.metoo.nspm.core.utils.query.PageInfo;
import com.metoo.nspm.dto.BackupSqlDTO;
import com.metoo.nspm.entity.nspm.Accessory;
import com.metoo.nspm.entity.nspm.BackupSql;
import com.metoo.nspm.entity.nspm.NetworkElement;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库维护
 */
@RequestMapping("/admin/backup/db")
@RestController
public class BackupSqlController {

    @Autowired
    private IBackupSqlService backupSqlService;

    // 系统剩余空间

    // 上传

    // 下载

    // 备份

    // 还原

    // 还原状态

    // 重置

    //

    // 命令行备份数据库
//    @GetMapping
//    public boolean backupDB(String fileName) {
//        // 命令行
//        fileName += ".sql";
//
//        String savePath = Global.DBPATH;
//
//        File saveFile = new File(Global.DBPATH);
//
//        if (!saveFile.exists()) {
//            saveFile.mkdirs();
//        }
//
//        if (!savePath.endsWith(File.separator)) {
//            savePath = savePath + File.separator;
//        }
//
//        //拼接命令行的命令
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("mysqldump").append(" --opt").append(" -h").append("nmap-mysql");
//        stringBuilder.append(" --user=").append("root").append(" --password=").append("metoo89745000")
//                .append(" --lock-all-tables=true");
//        stringBuilder.append(" --result-file=").append(savePath + fileName).append(" --default-character-set=utf8 ")
//                .append("nmap");
//        // 追加表名
//
//        stringBuilder.append(" rsms_terminal rsms_device ");
//
//        try {
//            System.out.println(stringBuilder.toString());
//            //调用外部执行exe文件的javaAPI
//            Process process = Runtime.getRuntime().exec(stringBuilder.toString());
//            if (process.waitFor() == 0) {// 0 表示线程正常终止。
//                return true;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    @PostMapping("/list")
    public Object list(@RequestBody(required = false) BackupSqlDTO dto){
        if(dto == null){
            dto = new BackupSqlDTO();
        }
        Page<BackupSql> page = this.backupSqlService.selectObjConditionQuery(dto);
        if(page.getResult().size() > 0){
            return ResponseUtil.ok(new PageInfo<BackupSql>(page));
        }
        return ResponseUtil.ok();
    }

    @GetMapping
    public Object backupDB(String name) {
        // 命令行
        String fileName = "nmap.sql";

        String savePath = Global.DBPATH + "/" + name;

        File saveFile = new File(savePath);

        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
//        else{
//            FileSystemUtils.deleteRecursively(saveFile);
//        }

        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }

        //拼接命令行的命令
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mysqldump").append(" --opt").append(" -h").append("nmap-mysql");
        stringBuilder.append(" --user=").append("root").append(" --password=").append("metoo89745000")
                .append(" --lock-all-tables=true");
        stringBuilder.append(" --result-file=").append(savePath + fileName).append(" --default-character-set=utf8 ")
                .append("nmap");
        // 追加表名

//        stringBuilder.append(" rsms_terminal rsms_device ");
        try {
            System.out.println(stringBuilder.toString());
            //调用外部执行exe文件的javaAPI
            Process process = Runtime.getRuntime().exec(stringBuilder.toString());
            if (process.waitFor() == 0) {// 0 表示线程正常终止
                // 创建记录
                BackupSql backupSql = this.backupSqlService.selectObjByName(name);
                if(backupSql == null){
                    backupSql = new BackupSql();
                }
                backupSql.setName(name);
                backupSql.setSize(this.getSize(Global.DBPATH + "/" + name));
                this.backupSqlService.save(backupSql);
                return ResponseUtil.ok();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseUtil.error();
    }

    @GetMapping("/get/size")
    public String size(){
       return this.getSize("/opt/nmap/resource/db/test");
    }

    public String getSize(String path){
        try {
            Process p = Runtime.getRuntime().exec("du -sh " + path);

            if (p.waitFor() == 0) {// 0 表示线程正常终止

                InputStream is = p.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;

                StringBuilder builder = new StringBuilder();

                while((line = reader.readLine())!= null){

                    builder.append(line);

                }

                try {
                    p.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                is.close();

                reader.close();

                p.destroy();

                if (builder.length()==0) {
                    return "";
                } else {
                    String str = builder.substring(0, builder.length() - System.lineSeparator().length());
                    if(str.indexOf("/") > -1){
                        return str.substring(0, str.indexOf("/")).trim();
                    }
                    return builder.substring(0, builder.length() - System.lineSeparator().length());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id){
        BackupSql backupSql = this.backupSqlService.selectObjById(id);
        if(backupSql != null){
            int i = this.backupSqlService.delete(backupSql.getId());
            if(i >= 1){
                String savePath = Global.DBPATH + "/" + backupSql.getName();
                File saveFile = new File(savePath);
                if (saveFile.exists()) {
                    try {
                        FileSystemUtils.deleteRecursively(saveFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return ResponseUtil.ok();
            }
            return ResponseUtil.error();
        }
        return ResponseUtil.badArgument();
    }
}
