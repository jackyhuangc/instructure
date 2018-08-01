package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.Member;
import com.jacky.strive.service.MemberService;
import com.jacky.strive.service.dto.MemberQueryDto;
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
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/{member_no}")
    public ResResult get(@PathVariable("member_no") String memberNo) {

        Member m = memberService.findByMemberNo(memberNo);
        AssertUtil.notNull(m, "用户不存在");

        return ResResult.success("", m);
    }

    @PostMapping("/register")
    public ResResult create(@RequestBody Member member) {

        Member m = memberService.add(member);
        AssertUtil.notNull(m, "注册失败");

        return ResResult.success("", m);
    }

    @PostMapping("/modify/{member_no}")
    public ResResult modify(@PathVariable("member_no") String memberNo, @RequestBody Member member) {

        member.setMemberNo(memberNo);
        Member m = memberService.modify(member);
        AssertUtil.notNull(m, "修改失败");

        return ResResult.success("", m);
    }

    @PostMapping("/modifyPassword/{member_no}/{org_pass}/{new_pass}")
    public ResResult modifyPassword(@PathVariable("member_no") String memberNo, @PathVariable("org_pass") String orgPass, @PathVariable("new_pass") String newPass) {

        boolean ret = memberService.modifyPassword(memberNo, orgPass, newPass);
        AssertUtil.isTrue(ret, "修改失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/activate/{member_no}/{active}")
    public ResResult activate(@PathVariable("member_no") String memberNo, @PathVariable("active") boolean active) {

        boolean ret = memberService.activate(memberNo, active);
        AssertUtil.isTrue(ret, "修改失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/query")
    public ResResult query(@RequestBody MemberQueryDto queryDto) {

        PageInfo<Member> memberList = memberService.findMemberList(queryDto);

        return ResResult.success("", memberList);
    }

    @GetMapping("/generateNewMemberNo")
    public ResResult generateNewMemberNo() {

        String ret = memberService.generateNewMemberNo();

        return ResResult.success("生成ID成功", ret);
    }
}
