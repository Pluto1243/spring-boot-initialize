package com.wj.boot.config.log;

/**
 * 日志操作类型
 *
 * @author wangjie
 * @date 15:54 2022年05月07日
 **/
public enum OperateType {
    /**
     * 添加
     */
    INSERT,

    /**
     * 删除
     */
    DELETE,

    /**
     * 查找
     */
    SELECT,

    /**
     * 更新
     */
    UPDATE,

    /**
     * 批量更新
     */
    PATCH_UPDATE,

    /**
     * 批量删除
     */
    PATCH_DELETE
}
