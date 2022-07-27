package com.wj.boot.config.log;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志实体类
 *
 * @author wangjie
 * @date 16:30 2022年05月07日
 **/
@Data
@TableName("log_info")
public class LogEntity implements Serializable {

    private Integer id;

    private Integer uid;

    private String name;

    private OperateType operateType;

    private String description;

    private String tableName;

    private String oldValue;

    private String newValue;

    private String ip;

    private String requestHeader;

    private Boolean result;

    private Date logAt;

    @Override
    public String toString() {
        return "LogEntity{" +
            "id=" + id +
            ", uin=" + uid +
            ", name='" + name + '\'' +
            ", operateType=" + operateType +
            ", description='" + description + '\'' +
            ", tableName='" + tableName + '\'' +
            ", oldValue='" + oldValue + '\'' +
            ", newValue='" + newValue + '\'' +
            ", ip='" + ip + '\'' +
            ", requestHeader='" + requestHeader + '\'' +
            ", result=" + result +
            ", logAt=" + logAt +
            '}';
    }
}
