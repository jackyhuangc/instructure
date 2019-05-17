package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.common.util.AssertUtil;
import com.jacky.common.entity.result.ResResult;
import com.jacky.strive.dao.model.KeyValue;
import com.jacky.strive.service.KeyValueService;
import com.jacky.strive.service.dto.KeyValueQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jacky.common.*;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@RestController
@RequestMapping("/keyValue")
public class KeyValueController {

    @Autowired
    KeyValueService keyValueService;

    @GetMapping("/{key}")
    public ResResult get(@PathVariable("key") String key) {

        KeyValue m = keyValueService.findByKey(key);
        AssertUtil.notNull(m, "产品不存在");

        return ResResult.success("", m);
    }

    @PostMapping("/create")
    public ResResult create(@RequestBody KeyValue keyValue) {

        KeyValue m = keyValueService.add(keyValue);
        AssertUtil.notNull(m, "添加失败");

        return ResResult.success("", m);
    }

    @PostMapping("/modify/{key}")
    public ResResult modify(@PathVariable("key") String key, @RequestBody KeyValue keyValue) {

        keyValue.setKeyvalueKey(key);
        KeyValue m = keyValueService.modify(keyValue);
        AssertUtil.notNull(m, "修改失败");

        return ResResult.success("", m);
    }

    @PostMapping("/delete/{key}")
    public ResResult delete(@PathVariable("key") String key) {

        boolean ret = keyValueService.delete(key);
        AssertUtil.isTrue(ret, "删除失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/query")
    public ResResult query(@RequestBody KeyValueQueryDto queryDto) {

        PageInfo<KeyValue> keyValueList = keyValueService.findKeyValueList(queryDto);

        return ResResult.success("", keyValueList);
    }
}