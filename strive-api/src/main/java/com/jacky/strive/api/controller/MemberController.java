package com.jacky.strive.api.controller;

import com.jacky.strive.dao.model.Member;
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
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/login")
    public ResResult login(MemberLoginDto loginDto) {
        return null;
    }

    @GetMapping("/query")
    public ResResult query(MemberQueryDto queryDto) {
        return null;
    }

    @GetMapping("/{member_no}")
    public ResResult get(@PathVariable("member_no") String memberNo) {
        return null;
    }

    @PostMapping("/register")
    public ResResult register(Member member) {
        return null;
    }

    @PutMapping("/modify/{member_no}")
    public ResResult modify(@PathVariable("member_no") String memberNo, Member member) {
        return null;
    }

    @PutMapping("/activate/{member_no}")
    public ResResult activate(@PathVariable("member_no") String memberNo, boolean active) {
        return null;
    }
}
