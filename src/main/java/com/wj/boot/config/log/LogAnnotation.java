package com.wj.boot.config.log;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义日志注解
 *
 * @author wangjie
 * @date 15:46 2022年05月07日
 **/
// 注解类型，注解位置
@Target(
    {ElementType.METHOD}
)
// 保留期 运行时
@Retention(
    RetentionPolicy.RUNTIME
)
@Component
public @interface LogAnnotation {

    String description() default "";

    String tableName() default "";

    OperateType operateType() default OperateType.SELECT;

    /**
     * 唯一键排序, 在多个参数的时候，指定唯一键或者含有唯一键的对象
     */
    int paramOrder() default 0;

}
