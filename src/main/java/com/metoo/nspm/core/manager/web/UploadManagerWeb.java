package com.metoo.nspm.core.manager.web;

import com.metoo.nspm.core.manager.admin.tools.DateTools;
import com.metoo.nspm.core.utils.ResponseUtil;
import com.metoo.nspm.entity.nspm.Accessory;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@RequestMapping("/web/upload")
@Controller
public class UploadManagerWeb {

    @PostMapping
    @ResponseBody
    public Object upload(
            @RequestParam(value = "file", required = false) MultipartFile file, String path){
        if(file != null && file.getSize() > 0){
            String result = this.uploadFile(file, path);
            return ResponseUtil.ok(result);
        }
        return ResponseUtil.badArgument();
    }

    public String uploadFile(@RequestParam(required = false) MultipartFile file, String path){
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));
        Date currentDate = new Date();
        String fileName1 = DateTools.getCurrentDate(currentDate);
        java.io.File imageFile = new File(path +  "/" + fileName1 + ext);
        if (!imageFile.getParentFile().exists()) {
            imageFile.getParentFile().mkdirs();
        }
        try {
            file.transferTo(imageFile);
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }
}
