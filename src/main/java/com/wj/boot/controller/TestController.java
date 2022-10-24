package com.wj.boot.controller;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.wj.boot.entity.Portrait;
import com.wj.boot.entity.response.R;
import com.wj.boot.mapper.MongoPortrait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoPortrait mongoPortrait;

    @PostMapping("save")
    public R<Portrait> save(@RequestBody Portrait portrait) {
        mongoTemplate.insert(portrait);
        return R.ok(portrait);
    }

    @PostMapping("saveByMapper")
    public R<Portrait> saveByMapper(@RequestBody Portrait portrait) {
        mongoPortrait.insert(portrait);
        return R.ok(portrait);
    }

    @PostMapping("update")
    public R<UpdateResult> update(@RequestBody Portrait portrait) {
        Criteria criteria = Criteria.where("resourceId").is(portrait.getResourceId());
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("tags", portrait.getTags());
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Portrait.class);
        return R.ok(updateResult);
    }

    @PostMapping("updateByMapper")
    public R updateByMapper(@RequestBody Portrait portrait) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("resourceId", ExampleMatcher.GenericPropertyMatcher::exact);
        Portrait query = new Portrait();
        query.setResourceId(portrait.getResourceId());
        Example<Portrait> example = Example.of(query, matcher);
        mongoPortrait.findOne(example).ifPresent(it -> {
            it.setTags(portrait.getTags());
            mongoPortrait.save(it);
        });
        return R.ok();
    }

    @PostMapping("get")
    public R<Portrait> get(@RequestBody Portrait portrait) {
        Criteria criteria = Criteria.where("resourceId").is(portrait.getResourceId());
        Query query = new Query(criteria);
        Portrait p = mongoTemplate.findOne(query, Portrait.class);
        return R.ok(p);
    }

    @PostMapping("getByMapper")
    public R<Portrait> getByMapper(@RequestBody Portrait portrait) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("resourceId", ExampleMatcher.GenericPropertyMatcher::exact);
        Portrait query = new Portrait();
        query.setResourceId(portrait.getResourceId());
        Example<Portrait> example = Example.of(query, matcher);
        return R.ok(mongoPortrait.findOne(example).get());
    }

    @PostMapping("delete")
    public R delete(@RequestBody Portrait portrait) {
        Criteria criteria = Criteria.where("resourceId").is(portrait.getResourceId());
        Query query = new Query(criteria);
        DeleteResult result = mongoTemplate.remove(query, Portrait.class);
        return R.ok(result);
    }

    @PostMapping("deleteByMapper")
    public R deleteByMapper(@RequestBody Portrait portrait) {
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("resourceId", ExampleMatcher.GenericPropertyMatcher::exact);
        Portrait query = new Portrait();
        query.setResourceId(portrait.getResourceId());
        Example<Portrait> example = Example.of(query, matcher);
        mongoPortrait.findOne(example).ifPresent(it -> {
            mongoPortrait.deleteById(it.getId());
        });
        return R.ok();
    }
}
