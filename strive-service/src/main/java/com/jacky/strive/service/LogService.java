package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.*;
import com.jacky.strive.dao.model.*;
import com.jacky.strive.service.dto.LogQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qsq.biz.common.util.AssertUtil;
import qsq.biz.common.util.DateUtil;
import qsq.biz.common.util.StringUtil;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author huangchao
 * @create 2018/6/6 上午11:50
 * @desc
 **/
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class LogService {

    @Autowired
    MemberLogDao memberLogDao;

    @Autowired
    KeyValueDao keyValueDao;

    public MemberLog add(MemberLog memberLog) {

        memberLog.setCreatedAt(DateUtil.now());
        int ret = memberLogDao.insert(memberLog);
        AssertUtil.isTrue(ret > 0, "添加日志失败");

        return ret > 0 ? memberLog : null;
    }

    public MemberLog findById(Integer logId) {
        Example example = new Example(MemberLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("logId", logId);

        MemberLog member = memberLogDao.selectOneByExample(example);

        return member;
    }

    public PageInfo<MemberLog> findMemberLogList(LogQueryDto queryDto) {

        Page<MemberLog> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(MemberLog.class);

        Example.Criteria criteria = example.createCriteria();

        if(!StringUtil.isEmtpy(queryDto.getMemberNo()))
        {
            criteria.andEqualTo("memberNo", queryDto.getMemberNo());
        }

        if(null!=queryDto.getLogType()&&queryDto.getLogType()>0)
        {
            criteria.andEqualTo("logType", queryDto.getLogType());
        }

        if(!StringUtil.isEmtpy(queryDto.getLogBusi()))
        {
            criteria.andEqualTo("logBusi", queryDto.getLogBusi());
        }

        example.and(criteria);
        example.setOrderByClause("created_at desc");
        List<MemberLog> memberList = memberLogDao.selectByExample(example);

        PageInfo<MemberLog> pageInfo = new PageInfo<>(memberList);

        return pageInfo;
    }
}