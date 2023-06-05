package com.metoo.nspm.dto;

import com.metoo.nspm.dto.page.PageDto;
import com.metoo.nspm.entity.nspm.BackupSql;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class BackupSqlDTO extends PageDto<BackupSql> {


    @ApiModelProperty("文件名称")
    private String name;
    @ApiModelProperty("文件大小")
    private String size;
}


