package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.User;
import com.jacky.strive.service.UserService;
import com.jacky.strive.service.dto.UserQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jacky.strive.common.*;
import com.jacky.strive.common.entity.ResResult;


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
        AssertUtil.notNull(u, "用户不存在");

        return ResResult.success("", u);
    }

    @PostMapping("/create")
    public ResResult create(@RequestBody User user) {

        User u = userService.add(user);
        AssertUtil.notNull(u, "创建失败");

        return ResResult.success("", u);
    }

    @PostMapping("/modify/{user_id}")
    public ResResult modify(@PathVariable("user_id") String userID, @RequestBody User user) {

        user.setUserId(userID);
        User u = userService.modify(user);
        AssertUtil.notNull(u, "修改失败");

        return ResResult.success("", u);
    }

    @PostMapping("/modifyPassword/{user_id}/{org_pass}/{new_pass}")
    public ResResult modifyPassword(@PathVariable("user_id") String userID, @PathVariable("org_pass") String orgPass, @PathVariable("new_pass") String newPass) {

        boolean ret = userService.modifyPassword(userID, orgPass, newPass);
        AssertUtil.isTrue(ret, "修改失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/activate/{user_id}/{active}")
    public ResResult activate(@PathVariable("user_id") String userID, @PathVariable("active") boolean active) {

        boolean ret = userService.activate(userID, active);
        AssertUtil.isTrue(ret, "修改失败");

        return ResResult.success("", ret);
    }

    @PostMapping("/delete/{user_id}")
    public ResResult delete(@PathVariable("user_id") String userID) {

        boolean ret = userService.delete(userID);
        AssertUtil.isTrue(ret, "删除失败");

        return ResResult.success("", ret);
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
