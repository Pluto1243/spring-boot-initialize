package com.wj.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.boot.config.log.LogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.TreeMap;

@Mapper
public interface LogMapper extends BaseMapper<LogEntity> {

    TreeMap executeSql(@Param("sql") String sql);
}
