package com.metoo.nspm.entity.nspm;

import com.metoo.nspm.core.domain.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.experimental.theories.DataPoints;

@Data
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class BackupSql extends IdEntity {

    @ApiModelProperty("文件名称")
    private String name;
    @ApiModelProperty("文件大小")
    private String size;

}
