package com.jacky.strive.api.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

//import javax.ws.rs.GET;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.Role;
import com.jacky.strive.dao.model.UserRole;
import com.jacky.strive.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import qsq.biz.common.util.AssertUtil;
import qsq.biz.scheduler.entity.ResResult;
//import com.jacky.jfutures.entities.ReturnResult;
//import com.jacky.jfutures.entities.Role;
//import com.jacky.jfutures.entities.RoleMapper;
//import com.jacky.jfutures.entities.SystemLog;
//import com.jacky.jfutures.entities.UserRole;
//import com.jacky.jfutures.entities.UserRoleMapper;
//import com.jacky.jfutures.utils.KafkaSender;

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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<String> addRole(@RequestBody Role role, Principal principal) throws IOException {

        Role ret = roleService.add(role);
        AssertUtil.notNull(ret, "角色创建失败");

        // 成功之后记录操作日志
        //service.sendMessage(JSON.toJSONString(
        //new SystemLog(principal.getName(), "Role Or Permission", "Delete", JSON.toJSONString(r), "")));

        return ResResult.success("",ret);
    }

    /**
     * 修改角色
     *
     * @param role 角色对象
     * @return 操作结果，成功可返回ID
     * @throws IOException
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<String> updateRole(@RequestBody Role role, Principal principal) throws IOException {

        Role ret = roleService.modify(role);
        AssertUtil.notNull(ret, "角色更新失败");

        return ResResult.success("",ret);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return 操作结果，成功可返回ID
     * @throws IOException
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<String> deleteRole(@RequestBody String roleId, Principal principal) throws IOException {

        boolean ret = roleService.enable(roleId, false);
        AssertUtil.isTrue(ret, "角色禁用失败");

        return ResResult.success("",ret);
    }

    /**
     * 查询所有角色信息
     *
     * @return 角色集合
     */
    @GetMapping("/queryRole")
    public ResResult<List<Role>> queryRole(String roleId) {

        PageInfo<Role> pageInfo = roleService.findRoleList(roleId);
        return ResResult.success("",pageInfo.getList());
    }

    /**
     * 查询指定用户角色信息
     *
     * @param userId 用户ID
     * @return 指定用户角色集合
     */
    @GetMapping("/queryUserRole")
    public ResResult<List<UserRole>> queryUserRole(String userId) {

        List<UserRole> userRoleList = roleService.findUserRoleList(userId);
        return ResResult.success("",userRoleList);
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

        return ResResult.success("",ret);
    }
}