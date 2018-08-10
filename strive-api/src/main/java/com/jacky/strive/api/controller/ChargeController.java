package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.MemberCharge;
import com.jacky.strive.service.ChargeService;
import com.jacky.strive.service.dto.ChargeQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qsq.biz.common.util.AssertUtil;
import qsq.biz.scheduler.entity.ResResult;

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
        charge.setChargeType(0);
        charge.setChargeStatus(2);
        MemberCharge m = chargeService.add(charge);
        AssertUtil.notNull(m, "充值失败");

        return ResResult.success("", m);
    }

    @PostMapping("/withdraw")
    public ResResult withdraw(MemberCharge charge) {
        charge.setChargeType(1);
        charge.setChargeStatus(2);
        MemberCharge m = chargeService.add(charge);
        AssertUtil.notNull(m, "提现失败");

        return ResResult.success("", m);
    }
}