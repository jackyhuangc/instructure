package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.KeyValueDao;
import com.jacky.strive.dao.RoleDao;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.dao.model.Member;
import com.jacky.strive.dao.model.Role;
import com.jacky.strive.dao.model.User;
import com.jacky.strive.service.dto.UserQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import qsq.biz.common.util.AssertUtil;
import qsq.biz.common.util.DateUtil;
import qsq.biz.common.util.Md5Util;
import qsq.biz.common.util.StringUtil;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
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
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleService roleService;

    @Autowired
    KeyValueDao keyValueDao;

    public User add(User user) {

        User u = findByUserName(user.getTelphone());
        AssertUtil.isTrue(null == u, "手机号已存在");

        user.setAddTime(DateUtil.now());
        user.setPassword(Md5Util.md5Encode(user.getTelphone().substring(user.getTelphone().length() - 6)));
        int ret = userDao.insert(user);
        return ret > 0 ? user : null;
    }

    public User modify(User user) {

        User u = findByUserName(user.getTelphone());
        AssertUtil.isTrue(null == u || u.getUserId().equals(user.getUserId()), "手机号已存在");

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", user.getUserId());

        int ret = userDao.updateByExampleSelective(user, example);
        return ret > 0 ? user : null;
    }

    public boolean modifyPassword(String userID, String orgPass, String newPass) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userID);

        User user = findByUserID(userID);
        AssertUtil.notNull(user, "用户不存在");
        AssertUtil.isTrue(user.getPassword().equals(orgPass) || user.getPassword().equals(Md5Util.md5Encode(orgPass)),
                "原密码不正确");

        user.setPassword(Md5Util.md5Encode(newPass));

        int ret = userDao.updateByExampleSelective(user, example);
        return ret > 0;
    }

    public boolean activate(String userID, boolean active) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userID);

        User user = findByUserID(userID);
        AssertUtil.notNull(user, "用户不存在");

        user.setIsActivated(active);

        int ret = userDao.updateByExampleSelective(user, example);
        return ret > 0;
    }

    public boolean delete(String userID) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userID);

        int ret = userDao.deleteByExample(example);
        return ret > 0;
    }

    public User findByUserName(String userName) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userName);
        criteria.orEqualTo("userName", userName);
        criteria.orEqualTo("telphone", userName);

        User user = userDao.selectOneByExample(example);
        return user;
    }

    public User findByUserID(String userID) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userID);

        User user = userDao.selectOneByExample(example);

        if (!StringUtil.isEmtpy(user.getUserRoles())) {
            PageInfo<Role> rolePageInfo = roleService.findRoleList("");

            StringBuilder permissionCodes = new StringBuilder();
            rolePageInfo.getList().stream().forEach(role -> {
                if (Arrays.asList(StringUtils.delimitedListToStringArray(user.getUserRoles(), ",")).contains(role.getRoleId())) {

                    permissionCodes.append(role.getPermissionCode());
                    if (!role.getPermissionCode().endsWith(",")) {
                        permissionCodes.append(",");
                    }
                }
            });
            user.setPermissionCode(permissionCodes.toString());
        }

        return user;
    }

    public PageInfo<User> findUserList(UserQueryDto queryDto) {

        Page<User> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(User.class);
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isActivated", true);
        criteria1.orIsNull("isActivated");

        Example.Criteria criteria2 = example.createCriteria();

        String condition = "";
        if (null != queryDto.getUserId()) {
            condition = "%" + queryDto.getUserId() + "%";
        }

        criteria2.andLike("userId", condition);
        criteria2.orLike("userName", condition);
        criteria2.orLike("telphone", condition);

        example.and(criteria2);
        example.setOrderByClause("add_time desc");
        List<User> userList = userDao.selectByExample(example);

        PageInfo<User> pageInfo = new PageInfo<User>(userList);

        return pageInfo;
    }

    public String generateNewUserID() {

        String maxUserId = keyValueDao.getDynamicResult("SELECT max(user_id) FROM `user`");

        if (!StringUtil.isEmtpy(maxUserId)) {
            maxUserId = "U" + String.format("%04d", Integer.valueOf(Integer.parseInt(maxUserId.substring(1)) + 1));
        } else {
            maxUserId = "U0001";
        }
        return maxUserId;
    }

    public String testConnnect() {
        Page<User> page = PageHelper.startPage(2, 2);
        Example exapl = new Example(User.class);
        Example.Criteria criteria1 = exapl.createCriteria();
        criteria1.andEqualTo("userName", "admin");
        List<User> users = userDao.selectByExample(exapl);

        PageInfo<User> pageInfo = new PageInfo<User>(users);

        return users.size() > 0 ? users.get(0).getUserName() : "Success！";
    }
}
