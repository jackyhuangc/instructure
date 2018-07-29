package com.jacky.strive.service;

import com.jacky.strive.dao.KeyValueDao;
import com.jacky.strive.dao.MemberDao;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.dao.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import qsq.biz.common.util.StringUtil;
import qsq.biz.scheduler.entity.ResResult;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author huangchao
 * @create 2018/6/6 上午11:50
 * @desc
 **/
@Service
@Scope("prototype")
public class MemberService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    KeyValueDao keyValueDao;

    public Member findByMemberNo(String memberNo) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberNo);
        criteria.orEqualTo("memberName", memberNo);

        Member m = memberDao.selectOneByExample(example);
        return m;
    }

    public ResResult findAll(String memberNo, String memberName) {
        return null;
    }

    public ResResult addMember(Member member) {
        return null;
    }

    public ResResult add(Member member) {
        return null;
    }

    public String generateNewMemberID() {

        String maxUserId = keyValueDao.getDynamicResult("SELECT max(member_id) FROM `member`");

        if (!StringUtil.isEmtpy(maxUserId)) {
            maxUserId = "M" + String.format("%06d", Integer.valueOf(Integer.parseInt(maxUserId.substring(1)) + 1));
        } else {
            maxUserId = "M000001";
        }
        return maxUserId;
    }
}
