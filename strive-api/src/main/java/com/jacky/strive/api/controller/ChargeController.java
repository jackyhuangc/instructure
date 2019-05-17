package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.common.util.AssertUtil;
import com.jacky.common.entity.result.ResResult;
import com.jacky.strive.dao.model.MemberCharge;
import com.jacky.strive.service.ChargeService;
import com.jacky.strive.service.MemberService;
import com.jacky.strive.service.dto.BindingReqDto;
import com.jacky.strive.service.dto.ChargeQueryDto;
import com.jacky.strive.service.enums.ChargeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jacky.common.*;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@RestController
@RequestMapping("/charge")
public class ChargeController {

    @Autowired
    ChargeService chargeService;

    @Autowired
    MemberService memberService;

    @GetMapping("/{charge_no}")
    public ResResult get(@PathVariable("charge_no") String chargeNo) {

        MemberCharge m = chargeService.findByChargeNo(chargeNo);
        AssertUtil.notNull(m, "数据不存在");

        return ResResult.success("", m);
    }

    @GetMapping("/query")
    public ResResult query(ChargeQueryDto queryDto) {

        PageInfo<MemberCharge> memberChargeList = chargeService.findMemberChargeList(queryDto);

        return ResResult.success("", memberChargeList);
    }

    @PostMapping("/charge")
    public ResResult charge(MemberCharge charge) {
        charge.setChargeType(ChargeTypeEnum.CHARGE.getValue());
        ResResult resResult = chargeService.add(charge);
        return resResult;
    }

    @PostMapping("/withdraw")
    public ResResult withdraw(MemberCharge charge) {
        charge.setChargeType(ChargeTypeEnum.WITHDRAW.getValue());
        ResResult resResult = chargeService.add(charge);
        return resResult;
    }

    @PostMapping("/bind_sms")
    public ResResult bindSms(@RequestBody BindingReqDto bindingReqDto) {
        return chargeService.bindSms(bindingReqDto);
    }

    @PostMapping("/bind")
    public ResResult bind(@RequestBody BindingReqDto bindingReqDto) {
        return chargeService.bind(bindingReqDto);
    }
}