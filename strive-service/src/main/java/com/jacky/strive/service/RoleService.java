package com.jacky.strive.service;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.KeyValueDao;
import com.jacky.strive.dao.RoleDao;
import com.jacky.strive.dao.UserRoleDao;
import com.jacky.strive.dao.model.Role;
import com.jacky.strive.dao.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.jacky.strive.common.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/5 21:09
 * @since jdk1.8
 */
@Service
@Scope("prototype")
public class RoleService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    KeyValueDao keyValueDao;

    public Role add(Role role) {
        int ret = roleDao.insert(role);
        return ret > 0 ? role : null;
    }

    public Role modify(Role role) {
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", role.getRoleId());

        int ret = roleDao.updateByExampleSelective(role, example);
        return ret > 0 ? role : null;
    }

    public boolean enable(String roleID, boolean enabled) {
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleID);

        Role role = findByRoleID(roleID);
        AssertUtil.notNull(role, "角色不存在");

        role.setIsEnabled(enabled);

        int ret = roleDao.updateByExampleSelective(role, example);
        return ret > 0;
    }

    public boolean delete(String roleID) {
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleID);

        int ret = roleDao.deleteByExample(example);
        return ret > 0;
    }

    public Role findByRoleID(String roleID) {
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleID);

        Role role = roleDao.selectOneByExample(example);
        return role;
    }


    public PageInfo<Role> findRoleList(String roleID) {

        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();

        if (null != roleID) {
            roleID = "%" + roleID + "%";
        }

        criteria.andEqualTo("isEnabled", true);
        criteria.andLike("roleId", roleID);

        List<Role> userList = roleDao.selectByExample(example);
        PageInfo<Role> pageInfo = new PageInfo<Role>(userList);

        return pageInfo;
    }


    public List<UserRole> findUserRoleList(String userID) {

        Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userID);

        List<UserRole> userRoleList = userRoleDao.selectByExample(example);

        return userRoleList;
    }

    public String generateRoleID() {

        String roleId = keyValueDao.getDynamicResult("SELECT max(role_id) FROM `role`");

        if (!StringUtil.isEmtpy(roleId)) {
            roleId = "R" + String.format("%03d", Integer.valueOf(Integer.parseInt(roleId.substring(1)) + 1));
        } else {
            roleId = "R001";
        }
        return roleId;
    }
}
