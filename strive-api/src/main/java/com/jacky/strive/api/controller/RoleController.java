package com.jacky.strive.api.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jacky.common.util.AssertUtil;
import com.jacky.strive.dao.model.Role;
import com.jacky.strive.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jacky.common.*;
import com.jacky.common.entity.result.ResResult;

/**
 * 角色权限管理控制器
 *
 * @author huang
 * @version 1.0 2017.12.15
 * @since jdk1.8
 */
@CrossOrigin()
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * 添加角色
     *
     * @param role 角色对象
     * @return 操作结果，成功可返回ID
     * @throws IOException
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<Role> addRole(@RequestBody Role role, Principal principal) throws IOException {

        Role ret = roleService.add(role);
        AssertUtil.notNull(ret, "角色创建失败");

        // 成功之后记录操作日志
        //service.sendMessage(JSON.toJSONString(
        //new SystemLog(principal.getName(), "Role Or Permission", "Delete", JSON.toJSONString(r), "")));

        return ResResult.success("", ret);
    }

    /**
     * 修改角色
     *
     * @param role 角色对象
     * @return 操作结果，成功可返回ID
     * @throws IOException
     */
    @RequestMapping(value = "/modify/{role_id}", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<Role> updateRole(@PathVariable("role_id") String roleId, @RequestBody Role role, Principal principal) throws IOException {

        Role ret = roleService.modify(role);
        AssertUtil.notNull(ret, "角色更新失败");

        return ResResult.success("", ret);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return 操作结果，成功可返回ID
     * @throws IOException
     */
    @RequestMapping(value = "/delete/{role_id}", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<Boolean> deleteRole(@PathVariable("role_id") String roleId, Principal principal) throws IOException {

        boolean ret = roleService.enable(roleId, false);
        AssertUtil.isTrue(ret, "角色禁用失败");

        return ResResult.success("", ret);
    }

    /**
     * 查询所有角色信息
     *
     * @return 角色集合
     */
    @GetMapping("/query/")
    public ResResult<List<Role>> query() {

        return query("");
    }

    /**
     * 查询所有角色信息
     *
     * @return 角色集合
     */
    @GetMapping("/query/{role_id}")
    public ResResult<List<Role>> query(@PathVariable("role_id") String roleId) {

        PageInfo<Role> pageInfo = roleService.findRoleList(roleId);
        return ResResult.success("", pageInfo.getList());
    }

    /**
     * 按规则，生成最新的用户ID
     *
     * @return 新用户ID
     */
    @GetMapping("/generateNewRoleID")
    public ResResult<String> generateNewRoleID() {

        String ret = roleService.generateRoleID();
        AssertUtil.notNull(ret, "生成ID失败");

        return ResResult.success("", ret);
    }
}