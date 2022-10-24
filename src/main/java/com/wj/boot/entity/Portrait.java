package com.wj.boot.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Portrait {

    @Id
    private String id;

    private Long resourceId;

    private String tags;

    private Long createTime;

    private Long updateTime;
}
