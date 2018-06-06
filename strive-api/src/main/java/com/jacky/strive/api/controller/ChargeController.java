package com.jacky.strive.api.controller;

import com.jacky.strive.dao.model.Member;
import com.jacky.strive.dao.model.MemberCharge;
import com.jacky.strive.service.dto.ChargeQueryDto;
import com.jacky.strive.service.dto.MemberLoginDto;
import com.jacky.strive.service.dto.MemberQueryDto;
import org.springframework.web.bind.annotation.*;
import qsq.biz.scheduler.entity.ResResult;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@RestController
@RequestMapping("/charge")
public class ChargeController {

    @GetMapping("/query")
    public ResResult query(ChargeQueryDto queryDto) {
        return null;
    }

    @GetMapping("/{charge_no}")
    public ResResult get(@PathVariable("charge_no") String chargeNo) {
        return null;
    }

    @PostMapping("/charge")
    public ResResult charge(MemberCharge charge) {
        return null;
    }

    @PostMapping("/withdraw")
    public ResResult withdraw(MemberCharge charge) {
        return null;
    }
}
