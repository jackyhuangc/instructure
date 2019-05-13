package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.Member;
import com.jacky.strive.dao.model.MemberAddress;
import com.jacky.strive.service.MemberService;
import com.jacky.strive.service.dto.LoginThirdDto;
import com.jacky.strive.service.dto.MemberQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.jacky.strive.common.*;
import com.jacky.strive.common.entity.ResResult;

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

    @PostMapping("/create")
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

    @PostMapping("/delete/{member_no}")
    public ResResult delete(@PathVariable("member_no") String memberNo) {

        boolean ret = memberService.delete(memberNo);
        AssertUtil.isTrue(ret, "删除失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/query")
    public ResResult query(@RequestBody MemberQueryDto queryDto) {

        PageInfo<Member> memberList = memberService.findMemberList(queryDto);

        return ResResult.success("", memberList);
    }

    @GetMapping("/address/{member_no}")
    public ResResult getDefault(@PathVariable("member_no") String memberNo) {

        MemberAddress m = memberService.findMemberAddressByDefault(memberNo);
        AssertUtil.notNull(m, "未设置默认地址");

        return ResResult.success("", m);
    }

    @PostMapping("/createAddress")
    public ResResult addAddress(@RequestBody MemberAddress memberAddress) {

        MemberAddress m = memberService.addAddress(memberAddress);
        AssertUtil.notNull(m, "添加失败");

        return ResResult.success("", m);
    }

    @PostMapping("/modifyAddress/{member_no}")
    public ResResult modifyAddress(@PathVariable("member_no") String memberNo, @RequestBody MemberAddress memberAddress) {

        memberAddress.setMemberNo(memberNo);
        MemberAddress m = memberService.modifyAddress(memberAddress);
        AssertUtil.notNull(m, "修改失败");

        return ResResult.success("", m);
    }


    @PostMapping("/deleteAddress/{member_no}")
    public ResResult deleteAddress(@PathVariable("member_no") String memberNo, @RequestBody MemberAddress memberAddress) {

        memberAddress.setMemberNo(memberNo);
        Integer ret = memberService.deleteAddress(memberNo, memberAddress.getAddressId());
        AssertUtil.notNull(ret > 0, "删除失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/queryAddress")
    public ResResult queryAddress(@RequestBody MemberQueryDto queryDto) {

        PageInfo<MemberAddress> memberList = memberService.findMemberAddressList(queryDto);

        return ResResult.success("", memberList);
    }

    @GetMapping("/generateNewMemberNo")
    public ResResult generateNewMemberNo() {

        String ret = memberService.generateNewMemberNo();

        return ResResult.success("生成ID成功", ret);
    }

    @PostMapping("/loginThird")
    public ResResult loginThird(@RequestBody LoginThirdDto loginThirdDto) {

        return memberService.loginThird(loginThirdDto);
    }
}
