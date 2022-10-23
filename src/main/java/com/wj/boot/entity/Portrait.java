package com.wj.boot.entity;

import lombok.Data;

@Data
public class Portrait {

    private Long resourceId;

    private String tags;

    private Long createTime;

    private Long updateTime;
}
