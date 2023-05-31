package com.metoo.nspm.entity.nspm;

import com.metoo.nspm.core.domain.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("配置备份")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigBackup extends IdEntity {

    private String host;// 主机Ip
    private String loginName;// 登录名
    private String loginPassword;// 登录密码
    private String enablePassword;// 通行密码
    private String fileHome;// 文件路径
    private String hostName;// 主机名
    private String uuid;// 网元Uuid
    private String backupTime;// 备份时间
    private String commType;// 设备类型
    private String vendor;// 品牌
    private String script;// 脚本文件
    private Integer port;
}
