package com.wj.boot.mapper;

import com.wj.boot.entity.Portrait;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 画像持久层
 *
 * @author wangjie
 * @date 21:50 2022年10月24日
 **/
@Mapper
public interface MongoPortrait extends MongoRepository<Portrait, String> {
}
