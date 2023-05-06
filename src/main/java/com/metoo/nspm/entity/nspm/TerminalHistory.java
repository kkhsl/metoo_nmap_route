package com.metoo.nspm.entity.nspm;

import com.metoo.nspm.core.domain.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalHistory extends IdEntity {

    private String ip;
    @ApiModelProperty("是否在线")
    private Boolean online;
}
