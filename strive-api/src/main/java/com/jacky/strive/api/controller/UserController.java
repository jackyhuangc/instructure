package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.User;
import com.jacky.strive.service.UserService;
import com.jacky.strive.service.dto.UserQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qsq.biz.common.util.DateUtil;
import qsq.biz.scheduler.entity.ResResult;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{user_id}")
    public ResResult get(@PathVariable("user_id") String userID) {

        User u = userService.findByUserID(userID);
        if (null == u) {
            return ResResult.fail("用户不存在");
        }

        return ResResult.success("", u);
    }

    @PostMapping("/create")
    public ResResult create(@RequestBody User user) {
        user.setAddTime(DateUtil.now());
        User u = userService.add(user);
        if (null == u) {
            return ResResult.fail("用户不存在");
        }

        return ResResult.success("", u);
    }

    @PostMapping("/modify/{user_id}")
    public ResResult modify(@PathVariable("user_id") String userID, @RequestBody User user) {
        user.setUserId(userID);
        User u = userService.modify(user);
        if (null == u) {
            return ResResult.fail("用户不存在");
        }

        return ResResult.success("", u);
    }

    @PostMapping("/modifyPassword/{user_id}/{org_pass}/{new_pass}")
    public ResResult modifyPassword(@PathVariable("user_id") String userID, @PathVariable("org_pass") String orgPass, @PathVariable("new_pass") String newPass) {

        if (!userService.modifyPassword(userID, orgPass, newPass)) {
            return ResResult.fail("密码修改失败");
        }

        return ResResult.success();
    }

    @PostMapping("/activate/{user_id}/{active}")
    public ResResult activate(@PathVariable("user_id") String userID, @PathVariable("active") boolean active) {

        if (!userService.activate(userID, active)) {
            return ResResult.fail("状态变更失败");
        }

        return ResResult.success();
    }

    @PostMapping("/query")
    public ResResult query(@RequestBody UserQueryDto queryDto) {

        PageInfo<User> userList = userService.findUserList(queryDto);
        return ResResult.success("", userList);
    }

    @GetMapping("/generateNewUserID")
    public ResResult generateNewUserID() {

        String ret = userService.generateNewUserID();
        return ResResult.success("生成ID成功", ret);
    }
}
