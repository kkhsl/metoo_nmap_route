package com.metoo.nspm.core.manager.admin.action;

import com.github.pagehelper.Page;
import com.metoo.nspm.core.manager.admin.tools.DateTools;
import com.metoo.nspm.core.service.nspm.IBackupSqlService;
import com.metoo.nspm.core.utils.Global;
import com.metoo.nspm.core.utils.MyStringUtils;
import com.metoo.nspm.core.utils.ResponseUtil;
import com.metoo.nspm.core.utils.file.DownLoadFileUtil;
import com.metoo.nspm.core.utils.file.FileUtil;
import com.metoo.nspm.core.utils.query.PageInfo;
import com.metoo.nspm.dto.BackupSqlDTO;
import com.metoo.nspm.entity.nspm.*;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        stringBuilder
                .append("mysqldump")
                .append(" --opt")
                .append(" -h")
                .append("nmap-mysql");
        stringBuilder
                .append(" --user=")
                .append("root")
                .append(" --password=")
                .append("metoo89745000")
                .append(" --lock-all-tables=true");
        stringBuilder
                .append(" --result-file=")
                .append(savePath + fileName)
                .append(" --default-character-set=utf8 ")
                .append("nmap");
        // 追加表名

        stringBuilder.append(" rsms_terminal rsms_device ");
        try {
            System.out.println(stringBuilder.toString());
            //调用外部执行exe文件的javaAPI
            Process process = Runtime.getRuntime().exec(stringBuilder.toString());
            process.waitFor(10000, TimeUnit.MILLISECONDS);
            // 0 表示线程正常终止
            if (process.waitFor() == 0) {
                // 创建记录
                BackupSql backupSql = this.backupSqlService.selectObjByName(name);
                if(backupSql == null){
                    backupSql = new BackupSql();
                }
                backupSql.setName(name);
                backupSql.setSize(this.getSize(Global.DBPATH + "/" + name));
                this.backupSqlService.save(backupSql);
                return ResponseUtil.ok();
            }else{
                //输出返回的错误信息
                StringBuffer mes = new StringBuffer();
                String tmp = "";
                BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while((tmp = error.readLine()) != null){
                    mes.append(tmp + "\n");
                }
                if(mes != null || !"".equals(mes) ){
                    System.out.println("备份成功!==>" + mes.toString());
                }
                error.close();
                return ResponseUtil.error(mes.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseUtil.error();
    }

    @PutMapping("/recover/{id}")
    public Object recoverSBackup(@PathVariable Long id){
        BackupSql backupSql = this.backupSqlService.selectObjById(id);
        if(backupSql != null){
            //拼接命令行的命令
            StringBuilder sb = new StringBuilder();
            sb.append("mysql");
            sb.append(" -h "+ "nmap-mysql");
            sb.append(" -P"+ "3306");
            sb.append(" -u"+ "root");
            sb.append(" -p"+ "metoo89745000");
            sb.append(" "+ "nmap" + " --default-character-set=utf8  < ");
            sb.append(Global.DBPATH + "/" + backupSql.getName() + "/" + "nmap" + ".sql");
//            sb.append("/opt/nmap/resource/db/backup1/nmap.sql");
            Process ps = null;
            InputStream is = null;
            BufferedReader bf = null;
            System.out.println(sb.toString());
            try {
                ps = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c", sb.toString()});
//                String[] command = {"/bin/sh", "-c", sb.toString()};
                // "mysql -hnmap-mysql -uroot -pmetoo89745000 nmap -e 'source /opt/nmap/resource/db/backup1/nmap.sql'"
//                process = Runtime.getRuntime().exec(command);
                is = ps.getInputStream();
                bf = new BufferedReader(new InputStreamReader(is,"utf8"));
                String line = null;
                StringBuffer msg = new StringBuffer();
                while ((line=bf.readLine())!=null){
                    System.out.println(line);
                    msg.append(line);
                }
                return ResponseUtil.ok(msg);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(bf != null){
                    try {
                        bf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ResponseUtil.badArgument();
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

    @Autowired
    private FileUtil fileUtil;

    // 上传
    @ApiOperation("上传")
    @RequestMapping("/upload")
    public Object uploadConfig(@RequestParam(value = "file", required = false) MultipartFile file, String name){
            if(file != null){
                boolean accessory = this.fileUtil.uploadFile(file, name,  ".sql", Global.DBPATH);
                if(accessory){
                    return ResponseUtil.ok();
                }else{
                    return ResponseUtil.error();
                }
            }
        return ResponseUtil.badArgument();
    }
    // 下载

    @GetMapping("/down/{id}")
    public Object down(@PathVariable Long id, HttpServletResponse response){
        BackupSql backupSql = this.backupSqlService.selectObjById(id);
        if(backupSql != null){
            String path = Global.DBPATH + File.separator + backupSql.getName() + File.separator + Global.DBNAME + ".sql";
            File file = new File(path);
            if (file.getParentFile().exists()) {
                boolean flag = DownLoadFileUtil.downloadZip(file, response);
                if (flag) {
                    return ResponseUtil.ok();
                } else {
                    return ResponseUtil.error("文件下载失败");
                }
            } else {
                return ResponseUtil.badArgument("文件下载失败");
            }
        }
        return ResponseUtil.badArgument();

    }
}
