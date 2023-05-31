package com.metoo.nspm.entity.nspm;

import com.metoo.nspm.core.domain.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("网元配置列表")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkElementConfig extends IdEntity {

    private String uuid;
    private String name;
}
