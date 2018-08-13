package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.*;
import com.jacky.strive.dao.model.*;
import com.jacky.strive.service.dto.MemberQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qsq.biz.common.util.AssertUtil;
import qsq.biz.common.util.DateUtil;
import qsq.biz.common.util.Md5Util;
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
public class MemberService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    MemberAddressDao memberAddressDao;

    @Autowired
    MemberVoucherDao memberVoucherDao;

    @Autowired
    MemberLogDao memberLogDao;

    @Autowired
    MemberChargeDao memberChargeDao;

    @Autowired
    KeyValueDao keyValueDao;


    public Member add(Member member) {

        Member u = findByMemberName(member.getMemberMobile());
        AssertUtil.isTrue(null == u, "手机号已存在");

        member.setCreatedAt(DateUtil.now());
        member.setMemberQuotas(0);
        member.setMemberPoints(0);
        member.setMemberPassword(Md5Util.md5Encode(member.getMemberMobile().substring(member.getMemberMobile().length() - 6)));
        int ret = memberDao.insert(member);

        if (null != member.getMemberAddress()) {
            addAddress(member.getMemberAddress());
        }

        return ret > 0 ? member : null;
    }

    public Member modify(Member member) {

        Member m = findByMemberName(member.getMemberMobile());
        AssertUtil.isTrue(null == m || m.getMemberNo().equals(member.getMemberNo()), "手机号已存在");

        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", member.getMemberNo());

        int ret = memberDao.updateByExampleSelective(member, example);
        return ret > 0 ? member : null;
    }

    public boolean modifyPassword(String memberNo, String orgPass, String newPass) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberNo);

        Member member = findByMemberNo(memberNo);
        AssertUtil.notNull(member, "用户不存在");
        AssertUtil.isTrue(member.getMemberPassword().equals(orgPass) || member.getMemberPassword().equals(Md5Util.md5Encode(orgPass)),
                "原密码不正确");

        member.setMemberPassword(Md5Util.md5Encode(newPass));

        int ret = memberDao.updateByExampleSelective(member, example);
        return ret > 0;
    }

    public boolean activate(String memberNo, boolean active) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberNo);

        Member member = findByMemberNo(memberNo);
        AssertUtil.notNull(member, "用户不存在");

        member.setMemberStatus(active);

        int ret = memberDao.updateByExampleSelective(member, example);
        return ret > 0;
    }

    public boolean delete(String memberNo) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberNo);

        Member member = findByMemberNo(memberNo);
        AssertUtil.notNull(member, "用户不存在");

        int ret = memberDao.deleteByExample(example);
        return ret > 0;
    }

    public Member findByMemberName(String memberName) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberName);
        criteria.orEqualTo("memberName", memberName);
        criteria.orEqualTo("memberMobile", memberName);

        Member member = memberDao.selectOneByExample(example);
        return member;
    }

    public Member findByMemberNo(String memberNo) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberNo);

        Member member = memberDao.selectOneByExample(example);

        return member;
    }

    public PageInfo<Member> findMemberList(MemberQueryDto queryDto) {

        Page<Member> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(Member.class);
//        Example.Criteria criteria1 = example.createCriteria();
//        criteria1.andEqualTo("memberStatus", true);
//        criteria1.orIsNull("memberStatus");

        Example.Criteria criteria2 = example.createCriteria();

        String condition = "%%";
        if (null != queryDto.getMemberNo()) {
            condition = "%" + queryDto.getMemberNo() + "%";
        }

        criteria2.andLike("memberNo", condition);
        criteria2.orLike("memberName", condition);
        criteria2.orLike("memberMobile", condition);

        example.and(criteria2);
        example.setOrderByClause("created_at desc");
        List<Member> memberList = memberDao.selectByExample(example);

        PageInfo<Member> pageInfo = new PageInfo<>(memberList);

        return pageInfo;
    }

    public String generateNewMemberNo() {

        String maxMemberNo = keyValueDao.getDynamicResult("SELECT max(member_no) FROM `member`");

        if (!StringUtil.isEmtpy(maxMemberNo)) {
            maxMemberNo = "M" + String.format("%06d", Integer.valueOf(Integer.parseInt(maxMemberNo.substring(1)) + 1));
        } else {
            maxMemberNo = "M000001";
        }
        return maxMemberNo;
    }

    public MemberAddress addAddress(MemberAddress memberAddress) {

        memberAddress.setCreatedAt(DateUtil.now());
        int ret = memberAddressDao.insert(memberAddress);
        AssertUtil.isTrue(ret > 0, "添加地址失败");

        return ret > 0 ? memberAddress : null;
    }

    public MemberVoucher addVoucher(MemberVoucher memberVoucher) {

        memberVoucher.setCreatedAt(DateUtil.now());
        int ret = memberVoucherDao.insert(memberVoucher);
        AssertUtil.isTrue(ret > 0, "添加代价券失败");

        return ret > 0 ? memberVoucher : null;
    }
}