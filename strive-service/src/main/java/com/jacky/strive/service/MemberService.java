package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.common.*;
import com.jacky.strive.common.entity.ResResult;
import com.jacky.strive.dao.*;
import com.jacky.strive.dao.model.*;
import com.jacky.strive.service.dto.LoginThirdDto;
import com.jacky.strive.service.dto.MemberQueryDto;
import com.jacky.strive.service.dto.WeChat.WxSession;
import com.jacky.strive.service.dto.WeChat.WxUserInfo;
import com.jacky.strive.service.enums.MemberTypeEnum;
import com.jacky.strive.service.utils.WXRequestUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
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


        if (StringUtil.isEmtpy(member.getMemberSource())) {
            member.setMemberSource("平台");
        }

        member.setCreatedAt(DateUtil.now());
        member.setMemberQuotas(BigDecimal.ZERO);
        member.setMemberPoints(0);
        member.setMemberDeposit(BigDecimal.ZERO);
        member.setMemberWithdraw(BigDecimal.ZERO);
        member.setMemberBuyAction(BigDecimal.ZERO);
        member.setMemberSellAction(BigDecimal.ZERO);
        member.setMemberInterestAction(BigDecimal.ZERO);
        if (!StringUtil.isEmtpy(member.getMemberPassword())) {
            if (member.getMemberPassword().length() <= 18) {
                member.setMemberPassword(Md5Util.encode(member.getMemberPassword()));
            }
        } else {
            member.setMemberPassword(Md5Util.encode(member.getMemberMobile().substring(member.getMemberMobile().length() - 6)));
        }
        int ret = memberDao.insert(member);

        if (!StringUtil.isEmtpy(member.getMemberAddress())) {

            MemberAddress address = new MemberAddress();
            address.setShippingDefault(true);
            address.setCreatedAt(DateUtil.now());
            address.setMemberNo(member.getMemberNo());
            address.setShippingUser(member.getMemberName());
            address.setShippingMobile(member.getMemberMobile());
            address.setShippingDistrict(member.getMemberDistrict());
            address.setShippingAddress(member.getMemberAddress());
            addAddress(address);
        }

        return ret > 0 ? member : null;
    }

    public Member modify(Member member) {

        Member m = findByMemberName(member.getMemberMobile());
        AssertUtil.isTrue(null == m || m.getMemberNo().equals(member.getMemberNo()), "手机号已存在");

        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", member.getMemberNo());

        member.setUpdatedAt(DateUtil.now());
        int ret = memberDao.updateByExampleSelective(member, example);
        return ret > 0 ? member : null;
    }

    public boolean modifyPassword(String memberNo, String orgPass, String newPass) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberNo);

        Member member = findByMemberNo(memberNo);
        AssertUtil.notNull(member, "用户不存在");
        AssertUtil.isTrue(member.getMemberPassword().equals(orgPass) || member.getMemberPassword().equals(Md5Util.encode(orgPass)),
                "原密码不正确");

        member.setMemberPassword(Md5Util.encode(newPass));

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

        // 同时清空收货地址
        MemberQueryDto queryDto = new MemberQueryDto();
        queryDto.setMemberNo(memberNo);
        PageInfo<MemberAddress> memberAddressList = findMemberAddressList(queryDto);
        int delRet = deleteAddress(memberNo, -1);

        return ret > 0 && delRet == memberAddressList.getList().size();
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

    public Member findByUnionId(String unionId) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("unionId", unionId);

        Member member = memberDao.selectOneByExample(example);
        return member;
    }

    public PageInfo<Member> findMemberList(MemberQueryDto queryDto) {

        Page<Member> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(Member.class);

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

        // 重新设置默认地址
        if (memberAddress.getShippingDefault()) {
            return modifyAddress(memberAddress);
        } else {

            MemberQueryDto queryDto = new MemberQueryDto();
            PageInfo<MemberAddress> pageInfo = findMemberAddressList(queryDto);

            // 只有一条数据时，一定要设置为默认
            if (pageInfo.getList().size() == 1) {
                memberAddress.setShippingDefault(true);
                return modifyAddress(memberAddress);
            }
        }

        return ret > 0 ? memberAddress : null;
    }

    public PageInfo<MemberAddress> findMemberAddressList(MemberQueryDto queryDto) {

        Example example = new Example(MemberAddress.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("memberNo", queryDto.getMemberNo());
        example.setOrderByClause("created_at desc");

        List<MemberAddress> addressList = memberAddressDao.selectByExample(example);

        return new PageInfo<>(addressList);
    }


    public MemberAddress findMemberAddressByDefault(String memberNo) {

        Example example = new Example(MemberAddress.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("memberNo", memberNo);
        criteria.andEqualTo("shippingDefault", true);

        MemberAddress address = memberAddressDao.selectOneByExample(example);

        return address;
    }

    public MemberAddress modifyAddress(MemberAddress memberAddress) {

        Example example = new Example(MemberAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberAddress.getMemberNo());

        List<MemberAddress> addressList = memberAddressDao.selectByExample(example);

        addressList.stream().forEach(address -> {
            if (memberAddress.getAddressId().equals(address.getAddressId())) {

                address.setShippingAddress(memberAddress.getShippingAddress());
                address.setShippingDistrict(memberAddress.getShippingDistrict());
                address.setShippingMobile(memberAddress.getShippingMobile());
                address.setShippingUser(memberAddress.getShippingUser());
                if (!memberAddress.getShippingDefault()) {
                    AssertUtil.isTrue(!address.getShippingDefault(), "未选择默认地址");
                }
                address.setShippingDefault(memberAddress.getShippingDefault());

            } else {
                if (memberAddress.getShippingDefault()) {
                    address.setShippingDefault(false);

                }
            }

            Example exp = new Example(MemberAddress.class);
            Example.Criteria crit = exp.createCriteria();
            crit.andEqualTo("addressId", address.getAddressId());
            int ret = memberAddressDao.updateByExampleSelective(address, exp);

            //AssertUtil.isTrue(ret > 0, "修改地址失败");
        });

        return memberAddress;
    }

    public Integer deleteAddress(String memberNo, Integer addressId) {
        Example example = new Example(MemberAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberNo);
        if (addressId > 0) {
            criteria.andEqualTo("addressId", addressId);
        }

        int ret = memberAddressDao.deleteByExample(example);
        return ret;
    }

    public ResResult loginThird(LoginThirdDto loginThirdDto) {

        if (loginThirdDto.getLoginType().equals("WeChat") || loginThirdDto.getLoginType().equals("QH")) {

            WxUserInfo wxUserInfo = loginThirdDto.getUserInfo();
            // 如果没有传入用户信息，则需要从微信服务器重新获取，这是正常的逻辑处理
            if (null == wxUserInfo) {

                ResResult<WxSession> resResult = WXRequestUtil.WxLogin(loginThirdDto.getCode());
                if (resResult.isSuccess()) {

                    // 用一下数据解析unionId等用户信息
                    String result = AesCbcUtil.decrypt(loginThirdDto.getEncryptedData(), loginThirdDto.getIv(), resResult.getData().getSessionKey(), "UTF-8");

                    wxUserInfo = JsonUtil.fromJson(result, WxUserInfo.class);
                }
            }

            AssertUtil.notNull(wxUserInfo, "用户验证失败");

            // 保存unionId到本地库，新增用户
            if (null == wxUserInfo.getUnionId()) {

                if (StringUtil.isEmtpy(wxUserInfo.getMobile())) {
                    wxUserInfo.setUnionId(loginThirdDto.getLoginType() + "_" + wxUserInfo.getNickName());
                } else {
                    wxUserInfo.setUnionId(loginThirdDto.getLoginType() + "_" + wxUserInfo.getMobile());
                }
            }

            Member member = findByUnionId(wxUserInfo.getUnionId());

            if (null == member) {
                if (StringUtil.isEmtpy(wxUserInfo.getPassword())) {
                    wxUserInfo.setPassword("");
                }
                if (StringUtil.isEmtpy(wxUserInfo.getMobile())) {
                    wxUserInfo.setMobile("");
                }

                member = new Member();
                member.setMemberNo(generateNewMemberNo());
                member.setMemberName(wxUserInfo.getNickName());
                member.setMemberImage(wxUserInfo.getAvatarUrl());
                member.setMemberType(MemberTypeEnum.普通.getValue());
                member.setMemberQuotas(BigDecimal.ZERO);
                member.setMemberPoints(0);
                member.setMemberPassword(wxUserInfo.getPassword());
                member.setMemberMobile(wxUserInfo.getMobile());

                if (StringUtil.isEmtpy(wxUserInfo.getProvince())) {
                    wxUserInfo.setProvince("");
                }
                if (StringUtil.isEmtpy(wxUserInfo.getCity())) {
                    wxUserInfo.setCity("");
                }

                member.setMemberDistrict(wxUserInfo.getProvince() + wxUserInfo.getCity());
                member.setMemberEmail("");
                member.setMemberStatus(true);
                // unionid适用于微信体系的所有账号，对于用户唯一
                member.setUnionId(wxUserInfo.getUnionId());
                // 此处的openid只记录最初创建的openid
                member.setOpenId(wxUserInfo.getOpenId());
                member.setMemberRemark(JsonUtil.toJson(wxUserInfo));
                member.setCreatedAt(DateUtil.now());
                member.setUpdatedAt(DateUtil.now());
                member.setMemberSource(loginThirdDto.getLoginType());

                member = add(member);
                AssertUtil.notNull(member, "用户创建失败");
            } else {
                // 用户可能换了另一个系统登录，比如之前是微信小程序，现在是微信公众号，他们的openid各不同
                member.setOpenId(wxUserInfo.getOpenId());
                member.setMemberName(wxUserInfo.getNickName());
                member.setMemberMobile(wxUserInfo.getMobile());
                member = modify(member);
            }

            return ResResult.success("", member);
        }

        return ResResult.fail("不支持的第三方登录");
    }
}