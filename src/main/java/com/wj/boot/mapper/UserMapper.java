package com.wj.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.boot.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * user映射类
 *
 * @author wangjie
 * @date 11:35 2022年07月26日
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
