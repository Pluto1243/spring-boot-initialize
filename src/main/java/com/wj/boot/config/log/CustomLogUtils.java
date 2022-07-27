package com.wj.boot.config.log;

import org.springframework.stereotype.Component;

/**
 * 自定义默认日志工具类 无法记录日志的方法可以通过调用此工具类实现添加日志
 *
 * @author wangjie
 * @date 11:16 2022年06月13日
 **/
@Component
public class CustomLogUtils {

    /**
     * @description 默认方法
     *
     * @author wangjie
     * @date 11:19 2022年06月13日
     * @param logInfo
     * @return void
     */
    @LogAnnotation
    (
        description = "test",
        tableName = "test",
            operateType = OperateType.INSERT
    )
    public void updateTask(String logInfo) {

    }
}
