package com.metoo.nspm.entity.nspm;

import com.metoo.nspm.core.domain.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HostSnmp extends IdEntity {

    @ApiModelProperty("主机可用性 0 - (默认) 未知; 1 - 可用; 2 - 不可用。")
    private String avaliable;
    @ApiModelProperty("网元Uuid")
    private String uuid;

}
