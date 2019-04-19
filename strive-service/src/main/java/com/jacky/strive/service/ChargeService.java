package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.common.BankCardUtil;
import com.jacky.strive.common.HttpUtil;
import com.jacky.strive.common.IdCardUtil;
import com.jacky.strive.common.SmsUtil;
import com.jacky.strive.dao.*;
import com.jacky.strive.dao.model.*;
import com.jacky.strive.service.dto.BindingReqDto;
import com.jacky.strive.service.dto.BindingRspDto;
import com.jacky.strive.service.dto.ChargeQueryDto;
import com.jacky.strive.service.enums.ChargeStatusEnum;
import com.jacky.strive.service.enums.ChargeTypeEnum;
import com.jacky.strive.service.utils.WXRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import qsq.biz.common.util.*;
import qsq.biz.scheduler.entity.ResResult;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Random;

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
    MemberCardDao memberCardDao;

    @Autowired
    LogService logService;

    @Autowired
    MemberService memberService;

    @Autowired
    KeyValueDao keyValueDao;

    @Autowired
    KeyValueService keyValueService;

    public ResResult add(MemberCharge charge) {

        Member member = memberService.findByMemberNo(charge.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        charge.setCreatedAt(DateUtil.now());
        int ret = memberChargeDao.insert(charge);
        AssertUtil.isTrue(ret > 0, "交易失败");

        if (charge.getChargeType().equals(ChargeTypeEnum.CHARGE.getValue()) && charge.getPayType().indexOf("WX_") > -1) {
            // 发送预支付链接
            return WXRequestUtil.PrePayment(charge.getChargeNo(), charge.getChargeQuotas(), charge.getPayType().substring(3), charge.getOpenId(), "会员充值");
        }

        return charge.getChargeStatus().equals(ChargeStatusEnum.STATUS_SUCCESS.getValue()) ? ResResult.success("交易成功", charge) : ResResult.process("处理中", charge);
    }

    public MemberCharge modify(Integer status, MemberCharge memberCharge) {
        Example example = new Example(MemberCharge.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("chargeNo", memberCharge.getChargeNo());
        criteria.andEqualTo("chargeStatus", status);

        int ret = memberChargeDao.updateByExample(memberCharge, example);
        AssertUtil.notNull(memberCharge, "用户不存在");

        return ret > 0 ? memberCharge : null;
    }

    public MemberCharge findByChargeNo(String chargeNo) {
        Example example = new Example(MemberCharge.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("chargeNo", chargeNo);

        MemberCharge memberCharge = memberChargeDao.selectOneByExample(example);

        return memberCharge;
    }

    public PageInfo<MemberCharge> findMemberChargeList(ChargeQueryDto queryDto) {

        Page<MemberCharge> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(MemberCharge.class);
        Example.Criteria criteria1 = example.createCriteria();

        if (null != queryDto.getChargeType()) {
            criteria1.andEqualTo("chargeType", queryDto.getChargeType());
        }

        if (null != queryDto.getChargeStatus()) {
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

    public ResResult bindSms(BindingReqDto bindingReqDto) {

        // 仅本地校验
        AssertUtil.isTrue(BankCardUtil.validate(bindingReqDto.getCardNum()), "银行卡号无效");
        AssertUtil.isTrue(IdCardUtil.validateIdCard18(bindingReqDto.getCardIdentity()), "身份证号无效");

        Example example = new Example(MemberCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cardNum", bindingReqDto.getCardNum());

        int ret = 0;
        MemberCard card = memberCardDao.selectOneByExample(example);
        if (null == card) {
            card = new MemberCard();
            card.setMemberNo(bindingReqDto.getMemberNo());
            card.setCardNum(bindingReqDto.getCardNum());
            card.setCardUser(bindingReqDto.getCardUser());
            card.setCardIdentity(bindingReqDto.getCardIdentity());
            card.setCardMobile(bindingReqDto.getCardMobile());
            card.setCardBank(bindingReqDto.getCardBank());
            card.setCardInfo(bindingReqDto.getCardInfo());
            card.setCreatedAt(DateUtil.now());
            card.setUpdatedAt(DateUtil.now());
            card.setCardStatus(false);
            card.setCardDefault(false);

            // 保存卡信息
            ret = memberCardDao.insert(card);
        } else {

            card.setMemberNo(bindingReqDto.getMemberNo());
            card.setCardNum(bindingReqDto.getCardNum());
            card.setCardUser(bindingReqDto.getCardUser());
            card.setCardIdentity(bindingReqDto.getCardIdentity());
            card.setCardMobile(bindingReqDto.getCardMobile());
            card.setCardBank(bindingReqDto.getCardBank());
            card.setCardInfo(bindingReqDto.getCardInfo());
            card.setCreatedAt(DateUtil.now());
            card.setUpdatedAt(DateUtil.now());
            card.setCardStatus(false);
            card.setCardDefault(false);

            // 修改卡信息
            ret = memberCardDao.updateByExample(card, example);
        }

        // 调用短信接口模拟发送绑卡短信
        String sources = "0123456789";
        Random rand = new Random();
        StringBuffer randNum = new StringBuffer();
        for (int j = 0; j < 6; j++) {
            randNum.append(sources.charAt(rand.nextInt(9)) + "");
        }

        String content = "您的验证码是[sms_code],请注意操作安全";
        content = content.replace("[sms_code]", randNum.toString());

        boolean sendResult = SmsUtil.sendSms(card.getCardMobile(), content);
        // #####################

        if (!sendResult) {
            return ResResult.fail("短信发送失败");
        }

        BindingRspDto rspDto = BeanUtil.map(card, BindingRspDto.class);
        rspDto.setVerifyCode(randNum.toString());
        rspDto.setVerifySeq("");

        return ret > 0 ? ResResult.success(rspDto) : ResResult.fail("操作失败");
    }

    public ResResult bind(BindingReqDto bindingReqDto) {

        // 模拟验证过程
        AssertUtil.isTrue(BankCardUtil.validate(bindingReqDto.getCardNum()), "银行卡号无效");
        AssertUtil.isTrue(IdCardUtil.validateIdCard18(bindingReqDto.getCardIdentity()), "身份证号无效");

        Example example = new Example(MemberCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cardNum", bindingReqDto.getCardNum());

        MemberCard card = memberCardDao.selectOneByExample(example);
        if (null == card) {
            return ResResult.fail("卡信息无效或不存在");
        }

        // 调用通道绑卡
        String protocolInfo = "1234567890";

        // 绑卡成功更新标识和协议号
        card.setCardInfo(protocolInfo);
        card.setCardStatus(true);
        memberCardDao.updateByExample(card, example);

        //memberCardDao.updateByExample()
        return ResResult.success(card);
    }

    public ResResult findCardByCardNum(String cardNum) {

        Example example = new Example(MemberCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cardNum", cardNum);

        MemberCard card = memberCardDao.selectOneByExample(example);
        if (null == card) {
            return ResResult.fail("卡信息无效或不存在");
        }

        return ResResult.success(card);
    }

    public ResResult findCardByMemberNo(String memberNo) {

        Example example = new Example(MemberCard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("memberNo", memberNo);
        //criteria.andEqualTo("cardStatus", true);

        example.setOrderByClause(" card_id desc");

        List<MemberCard> cardList = memberCardDao.selectByExample(example);
        if (CollectionUtils.isEmpty(cardList)) {
            return ResResult.fail("卡信息不存在");
        }

        return ResResult.success(cardList.get(0));
    }

    public ResResult channelChargeQuery(MemberCharge charge) {

        return ResResult.success("模拟充值成功");
    }

    public ResResult channelCharge(MemberCharge charge) {
        return null;
    }

    public ResResult channelWithdrawQuery(MemberCharge charge) {

        return ResResult.success("模拟提现成功");
    }

    public ResResult channelWithdraw(MemberCharge charge) {
        return null;
    }
}