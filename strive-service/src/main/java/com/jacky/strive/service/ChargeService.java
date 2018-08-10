package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.*;
import com.jacky.strive.dao.model.*;
import com.jacky.strive.service.dto.ChargeQueryDto;
import com.jacky.strive.service.enums.ChargeTypeEnum;
import com.jacky.strive.service.enums.LogBusiEnum;
import com.jacky.strive.service.enums.LogTypeEnum;
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
public class ChargeService {

    @Autowired
    MemberChargeDao memberChargeDao;

    @Autowired
    LogService logService;

    @Autowired
    MemberService memberService;

    @Autowired
    KeyValueDao keyValueDao;

    public MemberCharge add(MemberCharge memberCharge) {

        Member member = memberService.findByMemberNo(memberCharge.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        memberCharge.setCreatedAt(DateUtil.now());
        int ret = memberChargeDao.insert(memberCharge);
        AssertUtil.isTrue(ret > 0, "充值/提现失败");

        // 添加日志
        MemberLog log = new MemberLog();
        log.setCreatedAt(DateUtil.now());
        log.setLogBusi(memberCharge.getChargeType().equals(ChargeTypeEnum.CHARGE.getValue()) ?
                LogBusiEnum.充值.getValue() : LogBusiEnum.提现.getValue());
        log.setLogType(memberCharge.getChargeQuotas() > 0 ?
                LogTypeEnum.QUOTAS.getValue() : LogTypeEnum.POINTS.getValue());
        log.setMemberNo(memberCharge.getMemberNo());
        log.setMemberQuotas(memberCharge.getChargeQuotas());
        log.setMemberPoints(memberCharge.getChargePoints());
        log.setUpdatedAt(DateUtil.now());
        log.setLogMemo("");
        log = logService.add(log);
        AssertUtil.notNull(log, "日志添加失败");

        // 修改账户余额
        member.setMemberQuotas(member.getMemberQuotas() + log.getMemberQuotas());
        member.setMemberPoints(member.getMemberPoints() + log.getMemberPoints());
        member = memberService.modify(member);
        AssertUtil.notNull(member, "账户修改失败");

        return memberCharge;
    }

    public boolean activate(String chargeNo, Integer status) {
        Example example = new Example(MemberCharge.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("chargeNo", chargeNo);

        MemberCharge memberCharge = findByChargeNo(chargeNo);
        AssertUtil.notNull(memberCharge, "用户不存在");

        memberCharge.setChargeStatus(status);

        int ret = memberChargeDao.updateByExampleSelective(memberCharge, example);
        return ret > 0;
    }

    public MemberCharge findByChargeNo(String chargeNo) {
        Example example = new Example(MemberCharge.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("chargeNo", chargeNo);

        MemberCharge memberCharge = memberChargeDao.selectOneByExample(example);

        return memberCharge;
    }

    public PageInfo<MemberCharge> findMemberChargeList(ChargeQueryDto queryDto) {

        Page<Member> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(MemberCharge.class);
        Example.Criteria criteria1 = example.createCriteria();

        if (null == queryDto.getChargeType()) {
            criteria1.andIsNull("chargeType");
        } else {
            criteria1.andEqualTo("chargeType", queryDto.getChargeType());
        }

        if (null == queryDto.getChargeStatus()) {
            criteria1.andIsNull("chargeStatus");
        } else {
            criteria1.andEqualTo("chargeStatus", queryDto.getChargeStatus());
        }

        Example.Criteria criteria2 = example.createCriteria();

        String condition = "%%";
        if (null != queryDto.getMemberNo()) {
            condition = "%" + queryDto.getMemberNo() + "%";
        }

        criteria2.andLike("memberNo", condition);
        criteria2.orLike("chargeNo", condition);

        example.and(criteria2);
        example.setOrderByClause("created_at desc");
        List<MemberCharge> memberList = memberChargeDao.selectByExample(example);

        PageInfo<MemberCharge> pageInfo = new PageInfo<>(memberList);

        return pageInfo;
    }

    public String generateNewChargeNo() {

        String maxChargeNo = keyValueDao.getDynamicResult("SELECT max(charge_no) FROM `member_charge`");

        if (!StringUtil.isEmtpy(maxChargeNo)) {
            maxChargeNo = "C" + String.format("%08d", Integer.valueOf(Integer.parseInt(maxChargeNo.substring(1)) + 1));
        } else {
            maxChargeNo = "C00000001";
        }
        return maxChargeNo;
    }
}