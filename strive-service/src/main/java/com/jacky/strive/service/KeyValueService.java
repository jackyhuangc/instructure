package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.common.AssertUtil;
import com.jacky.strive.common.DateUtil;
import com.jacky.strive.dao.KeyValueDao;
import com.jacky.strive.dao.model.KeyValue;
import com.jacky.strive.service.dto.KeyValueQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author huangchao
 * @create 2018/6/6 上午11:50
 * @desc
 **/
@Service
@Scope("prototype")
public class KeyValueService {

    @Autowired
    KeyValueDao keyValueDao;


    public KeyValue add(KeyValue keyValue) {

        KeyValue u = findByKey(keyValue.getKeyvalueKey());
        AssertUtil.isTrue(null == u, "KV已存在");

        keyValue.setCreatedAt(DateUtil.now());

        int ret = keyValueDao.insert(keyValue);
        return ret > 0 ? keyValue : null;
    }

    public KeyValue modify(KeyValue keyValue) {

        KeyValue kv = findByKey(keyValue.getKeyvalueKey());
        AssertUtil.notNull(kv, "KV不存在");

        Example example = new Example(KeyValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("keyvalueKey", keyValue.getKeyvalueKey());

        int ret = keyValueDao.updateByExampleSelective(keyValue, example);
        return ret > 0 ? keyValue : null;
    }

    public boolean delete(String key) {

        KeyValue keyValue = findByKey(key);
        AssertUtil.notNull(keyValue, "KV不存在");

        Example example = new Example(KeyValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("keyvalueKey", key);

        int ret = keyValueDao.deleteByExample(example);
        return ret > 0;
    }

    public KeyValue findByKey(String key) {
        Example example = new Example(KeyValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("keyvalueKey", key);

        KeyValue keyValue = keyValueDao.selectOneByExample(example);
        return keyValue;
    }

    public PageInfo<KeyValue> findKeyValueList(KeyValueQueryDto queryDto) {

        Page<KeyValue> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(KeyValue.class);
        Example.Criteria criteria = example.createCriteria();

        String condition = "%%";
        if (null != queryDto.getKeyvalueKey()) {
            condition = "%" + queryDto.getKeyvalueKey() + "%";
        }

        criteria.andLike("keyvalueKey", condition);
        criteria.orLike("keyvalueValue", condition);

        example.setOrderByClause("created_at desc");
        List<KeyValue> keyValueList = keyValueDao.selectByExample(example);

        PageInfo<KeyValue> pageInfo = new PageInfo<>(keyValueList);

        return pageInfo;
    }
}