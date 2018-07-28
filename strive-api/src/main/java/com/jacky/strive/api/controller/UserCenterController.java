//package com.jacky.mybatis.dao.api.controller;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.security.Principal;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import com.alibaba.fastjson.JSON;
//import com.jacky.jfutures.entities.ReturnResult;
//import com.jacky.jfutures.entities.Role;
//import com.jacky.jfutures.entities.RoleMapper;
//import com.jacky.jfutures.entities.SystemLog;
//import com.jacky.jfutures.entities.User;
//import com.jacky.jfutures.entities.UserMapper;
//import com.jacky.jfutures.entities.UserRole;
//import com.jacky.jfutures.entities.UserRoleMapper;
////import com.jacky.jfutures.utils.KafkaSender;
//import com.jackyhuangc.global.utils.Md5Util;
//
///**
// * 用户中心
// *
// * @author huang
// * @version 1.0 2017.12.15
// * @since jdk1.8
// */
//@CrossOrigin()
//@RestController
//public class UserCenterController {
//
//	// private static final Logger logger =
//	// LoggerFactory.getLogger(UserCenterController.class);
//
//	/**
//	 * Kafka发送服务 需要使用service时，再使用@Autowired进行自动配置实例化
//	 */
//	//@Autowired
//	//private KafkaSender service;
//
//	/**
//	 * 新增用户
//	 *
//	 * @param user
//	 *            用户信息
//	 * @param principal
//	 *            系统身份
//	 * @return 操作结果，成功可返回用户ID
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/user/addUser", method = RequestMethod.POST)
//	@ResponseBody
//	public ReturnResult<String> addUser(@RequestBody User user, Principal principal) throws IOException {
//
//		// 读取mybatis配置文件
//		Reader reader = Resources.getResourceAsReader("mybatisconfig_mysql.xml");
//
//		// 创建
//		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//
//		// 解析资源
//		SqlSessionFactory factory = builder.build(reader);
//
//		// 打开session autoCommit默认为false，即手动提交
//		SqlSession session = factory.openSession();
//
//		try {
//
//			// 用接口映射的方式进行CURD操作，官方推荐
//			UserMapper userMapper = session.getMapper(UserMapper.class);
//			UserRoleMapper userRoleMapper = session.getMapper(UserRoleMapper.class);
//
//			user.setAddTime(new Date());
//
//			user.setModifyTime(new Date());
//
//			User u = userMapper.getById(user.getUserID());
//			if (u != null) {
//				return new ReturnResult<String>("001", "exists the same user id!", "");
//			}
//
//			u = userMapper.getByEmailOrTelphone(user.getEmail(), user.getTelphone());
//			if (u != null && u.getEmail().equals(user.getEmail())) {
//				return new ReturnResult<String>("001", "exists the same user email!", "");
//			}
//
//			if (u != null && u.getTelphone().equals(user.getTelphone())) {
//				return new ReturnResult<String>("001", "exists the same user telphone!", "");
//			}
//
//			if (user.getTelphone() != null && !user.getTelphone().isEmpty() && user.getTelphone().length() > 6) {
//
//				// 手机后六位作密码
//				user.setPassword(Md5Util.encode(user.getTelphone().substring(user.getTelphone().length() - 6)));
//			} else {
//				// 默认六位密码123456
//				user.setPassword(Md5Util.encode("123456"));
//			}
//
//			// mapper文件中的update,delete,insert语句是不需要设置返回类型的，它们都是默认返回一个int
//			int ret = userMapper.add(user);
//			if (ret <= 0) {
//				session.rollback();
//				return new ReturnResult<String>("001", "add failed!", "");
//			}
//
//			// 此时的userid已经修改成了数据库中的uuid()类型
//			// user.getUserID();
//
//			List<String> userRoles = new ArrayList<String>();
//
//			if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
//				userRoles = Arrays.asList(user.getUserRoles().split(","));
//			}
//
//			// 依次添加角色
//			for (String r : userRoles) {
//				ret = userRoleMapper.add(new UserRole(user.getUserID(), r));
//				if (ret <= 0) {
//					session.rollback();
//					return new ReturnResult<String>("001", "add role failed!", "");
//				}
//			}
//
//			session.commit();
//
//			// 成功之后记录操作日志
//			//service.sendMessage(JSON.toJSONString(
//					//new SystemLog(principal.getName(), "User Center", "Add", JSON.toJSONString(user), "")));
//
//		} catch (Exception e) {
//			return new ReturnResult<String>("001", e.getMessage(), "");
//		} finally {
//			// 没close之前是不会提交到数据库的
//			session.close();
//		}
//
//		return new ReturnResult<String>("000", "", "");
//	}
//
//	/**
//	 * 修改用户
//	 *
//	 * @param user
//	 *            用户信息
//	 * @param principal
//	 *            系统身份
//	 * @return 操作结果，成功可返回用户ID
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/user/updateUser", method = RequestMethod.POST)
//	@ResponseBody
//	public ReturnResult<String> updateUser(@RequestBody User user, Principal principal) throws IOException {
//
//		// 读取mybatis配置文件
//		Reader reader = Resources.getResourceAsReader("mybatisconfig_mysql.xml");
//
//		// 创建
//		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//
//		// 解析资源
//		SqlSessionFactory factory = builder.build(reader);
//
//		// 打开session autoCommit默认为false，即手动提交
//		SqlSession session = factory.openSession();
//
//		try {
//
//			// 用接口映射的方式进行CURD操作，官方推荐
//			UserMapper userMapper = session.getMapper(UserMapper.class);
//			UserRoleMapper userRoleMapper = session.getMapper(UserRoleMapper.class);
//
//			User oriUser = userMapper.getById(user.getUserID());
//
//			// 之前的数据
//			if (oriUser == null) {
//				return new ReturnResult<String>("001", "can not found user!", "");
//			}
//
//			// 在mapper.xml文件中定义具体的sql指令，不要嫌麻烦
//			// u.setUserName(user.getUserName());
//			// u.setTelphone(user.getTelphone());
//			// u.setEmail(user.getEmail());
//			// u.setImage(user.getImage());
//			// u.setCountry(user.getCountry());
//			// u.setAddress(user.getAddress());
//			// u.setExpiredTime(user.getExpiredTime());
//			// u.setIsActivated(user.getIsActivated());
//			// u.setLastLogin(user.getLastLogin());
//			// u.setRemark(user.getRemark());
//
//			int ret = userMapper.update(user);
//			if (ret <= 0) {
//				session.rollback();
//				return new ReturnResult<String>("001", "update failed!", "");
//			}
//
//			List<String> userRoles = new ArrayList<String>();
//
//			if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
//				userRoles = Arrays.asList(user.getUserRoles().split(","));
//			}
//
//			List<UserRole> listUserRole = userRoleMapper.queryAllByUserId(user.getUserID());
//			for (UserRole ur : listUserRole) {
//				// 没有找到之前的角色，则删除
//				if (!userRoles.contains(ur.getRoleID())) {
//					ret = userRoleMapper.delete(ur.getUserID(), ur.getRoleID());
//					if (ret <= 0) {
//						session.rollback();
//						return new ReturnResult<String>("001", "update role failed!", "");
//					}
//				}
//			}
//
//			for (String r : userRoles) {
//				boolean isExist = false;
//				for (UserRole ur : listUserRole) {
//					if (ur.getRoleID().contains(r)) {
//						isExist = true;
//					}
//				}
//
//				// 没有找到最新的角色，则添加
//				if (!isExist) {
//					ret = userRoleMapper.add(new UserRole(user.getUserID(), r));
//					if (ret <= 0) {
//						session.rollback();
//						return new ReturnResult<String>("001", "update role failed!", "");
//					}
//				}
//			}
//
//			session.commit();
//
//			// 成功之后记录操作日志
//			//service.sendMessage(JSON.toJSONString(new SystemLog(principal.getName(), "User Center", "Update",
//					//JSON.toJSONString(user), JSON.toJSONString(oriUser))));
//
//		} catch (Exception e) {
//			return new ReturnResult<String>("001", e.getMessage(), "");
//		} finally {
//			// 没close之前是不会提交到数据库的
//			session.close();
//		}
//
//		return new ReturnResult<String>("000", "", "");
//	}
//
//	/**
//	 * 删除用户 注意: @RequestBody才能解析post请求中的body参数
//	 *
//	 * @param userID
//	 *            用户ID
//	 * @param principal
//	 *            系统身份
//	 * @return 操作结果，成功可返回用户ID
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/user/deleteUser", method = RequestMethod.POST)
//	@ResponseBody
//	public ReturnResult<String> deleteUser(@RequestBody String userID, Principal principal) throws IOException {
//
//		// 读取mybatis配置文件
//		Reader reader = Resources.getResourceAsReader("mybatisconfig_mysql.xml");
//
//		// 创建
//		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//
//		// 解析资源
//		SqlSessionFactory factory = builder.build(reader);
//
//		// 打开session autoCommit默认为false，即手动提交
//		SqlSession session = factory.openSession();
//
//		try {
//
//			// 用接口映射的方式进行CURD操作，官方推荐
//			UserMapper userMapper = session.getMapper(UserMapper.class);
//			UserRoleMapper userRoleMapper = session.getMapper(UserRoleMapper.class);
//
//			User user = userMapper.getById(userID);
//			if (user == null || user.getUserID() == null || user.getUserID().isEmpty()) {
//				return new ReturnResult<String>("001", "can not found user!", "");
//			}
//
//			int ret = 0;
//			List<UserRole> listUserRole = userRoleMapper.queryAllByUserId(user.getUserID());
//			for (UserRole ur : listUserRole) {
//				// 依次删除所有角色
//				ret = userRoleMapper.delete(ur.getUserID(), ur.getRoleID());
//				if (ret <= 0) {
//					session.rollback();
//					return new ReturnResult<String>("001", "delete role failed!", "");
//				}
//			}
//
//			// 再删除用户
//			ret = userMapper.delete(userID);
//			if (ret <= 0) {
//				session.rollback();
//				return new ReturnResult<String>("001", "delete failed!", "");
//			}
//
//			session.commit();
//
//			// 成功之后记录操作日志
//			//service.sendMessage(JSON.toJSONString(
//					//new SystemLog(principal.getName(), "User Center", "Delete", JSON.toJSONString(user), "")));
//
//		} catch (Exception e) {
//			return new ReturnResult<String>("001", e.getMessage(), "");
//		} finally {
//			// 没close之前是不会提交到数据库的
//			session.close();
//		}
//
//		return new ReturnResult<String>("000", "", "");
//	}
//
//	/**
//	 * 修改密码
//	 *
//	 * @param user
//	 *            用户ID
//	 * @param principal
//	 *            系统身份
//	 * @return 操作结果，成功可返回用户ID
//	 * @throws IOException
//	 */
//	@RequestMapping(value = "/user/modifyPassword", method = RequestMethod.POST)
//	@ResponseBody
//	public ReturnResult<String> modifyPassword(@RequestBody User user, Principal principal) throws IOException {
//
//		// 读取mybatis配置文件
//		Reader reader = Resources.getResourceAsReader("mybatisconfig_mysql.xml");
//
//		// 创建
//		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//
//		// 解析资源
//		SqlSessionFactory factory = builder.build(reader);
//
//		// 打开session autoCommit默认为false，即手动提交
//		SqlSession session = factory.openSession();
//
//		try {
//
//			// 用接口映射的方式进行CURD操作，官方推荐
//			UserMapper userMapper = session.getMapper(UserMapper.class);
//
//			User u = userMapper.getById(user.getUserID());
//			if (u == null || u.getUserID() == null || u.getUserID().isEmpty()) {
//				return new ReturnResult<String>("001", "can not found user!", "");
//			}
//
//			int ret = userMapper.modifyPassword(user.getUserID(), Md5Util.encode(user.getPassword()));
//			if (ret <= 0) {
//				session.rollback();
//				return new ReturnResult<String>("001", "modify password failed!", "");
//			}
//
//			session.commit();
//
//			// 成功之后记录操作日志
//			//service.sendMessage(JSON.toJSONString(
//					//new SystemLog(principal.getName(), "User Center", "Modify Password", JSON.toJSONString(user), "")));
//
//		} catch (Exception e) {
//			return new ReturnResult<String>("001", e.getMessage(), "");
//		} finally {
//			// 没close之前是不会提交到数据库的
//			session.close();
//		}
//
//		return new ReturnResult<String>("000", "", "");
//	}
//
//	/**
//	 * 按规则，生成最新的用户ID
//	 *
//	 * @return 新用户ID
//	 */
//	@GET
//	@RequestMapping("/user/generateNewUserID")
//	@Produces(MediaType.APPLICATION_JSON)
//	public ReturnResult<String> generateNewUserID() {
//
//		String newUserID = "";
//		try {
//
//			// 读取mybatis配置文件
//			Reader reader = Resources.getResourceAsReader("mybatisconfig_mysql.xml");
//
//			// 创建
//			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//
//			// 解析资源
//			SqlSessionFactory factory = builder.build(reader);
//
//			// 打开session
//			SqlSession session = factory.openSession();
//
//			// 用接口映射的方式进行CURD操作，官方推荐
//			UserMapper userMapper = session.getMapper(UserMapper.class);
//
//			newUserID = userMapper.generateNewUserID();
//
//			if (newUserID == null || newUserID.isEmpty()) {
//				return new ReturnResult<String>("001", "generate userid failed!", "");
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			return new ReturnResult<String>("001", e.toString(), "");
//		}
//
//		return new ReturnResult<String>("000", "", newUserID);
//	}
//
//	/**
//	 * 根据用户ID查询用户信息
//	 *
//	 * @param userID
//	 *            用户ID
//	 * @return 用户信息
//	 */
//	@GET
//	@RequestMapping("/user/getUser")
//	@Produces(MediaType.APPLICATION_JSON)
//	public ReturnResult<User> getUser(String userID) {
//		User result = null;
//
//		try {
//
//			// 读取mybatis配置文件
//			Reader reader = Resources.getResourceAsReader("mybatisconfig_mysql.xml");
//
//			// 创建
//			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//
//			// 解析资源
//			SqlSessionFactory factory = builder.build(reader);
//
//			// 打开session
//			SqlSession session = factory.openSession();
//
//			// 用接口映射的方式进行CURD操作，官方推荐
//			UserMapper userMapper = session.getMapper(UserMapper.class);
//			RoleMapper roleMapper = session.getMapper(RoleMapper.class);
//			UserRoleMapper userRoleMapper = session.getMapper(UserRoleMapper.class);
//
//			result = userMapper.getById(userID);
//			if (result != null) {
//				List<UserRole> listUserRole = userRoleMapper.queryAllByUserId(result.getUserID());
//				String permissionCodes="";
//				for(UserRole uRole:listUserRole)
//				{
//					Role role=roleMapper.getById(uRole.getRoleID());
//					if(role!=null&&role.getPermissionCode()!=null&&!role.getPermissionCode().isEmpty())
//					{
//						List<String> rolePermissionCodes = Arrays.asList(role.getPermissionCode().split(","));
//						for(String pCode:rolePermissionCodes)
//						{
//							// 如果用户没有该权限，则添加
//							if(!Arrays.asList(permissionCodes.split(",")).contains(pCode))
//							{
//								permissionCodes+=pCode+",";
//							}
//						}
//					}
//				}
//
//				result.setPermissionCode(permissionCodes);
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			return new ReturnResult<User>("001", e.toString(), result);
//		}
//
//		return new ReturnResult<User>("000", "", result);
//	}
//
//	/**
//	 * 根据条件查询用户信息
//	 *
//	 * @param bgtm
//	 *            注册开始时间
//	 * @param edtm
//	 *            注册截止时间
//	 * @param country
//	 *            国籍
//	 * @param userID
//	 *            用户ID
//	 * @param userName
//	 *            用户名
//	 * @param email
//	 *            邮箱
//	 * @param count
//	 *            总数
//	 * @param page
//	 *            页码
//	 * @param size
//	 *            每页数量
//	 * @return 用户集合
//	 */
//	@GET
//	@RequestMapping("/user/queryUser")
//	@Produces(MediaType.APPLICATION_JSON)
//	public ReturnResult<List<User>> queryUser(String bgtm, String edtm, String country, String userID, String userName,
//			String email, Long count, int page, int size) {
//		List<User> listResult = new ArrayList<User>();
//
//		if (bgtm == null || bgtm.isEmpty()) {
//			bgtm = "1900-01-01 00:00:00";
//		}
//		if (edtm == null || edtm.isEmpty()) {
//			edtm = "2099-12-31 23:59:59";
//		}
//
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			Date begin = null;
//			Date end = null;
//			try {
//				begin = sdf.parse(bgtm);
//				end = sdf.parse(edtm);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//
//			// 读取mybatis配置文件
//			Reader reader = Resources.getResourceAsReader("mybatisconfig_mysql.xml");
//
//			// 创建
//			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//
//			// 解析资源
//			SqlSessionFactory factory = builder.build(reader);
//
//			// 打开session
//			SqlSession session = factory.openSession();
//
//			// 用接口映射的方式进行CURD操作，官方推荐
//			UserMapper userMapper = session.getMapper(UserMapper.class);
//
//			if (count < 0) {
//
//				// count = (long) list.getNumberOfElements();
//				// if (count >= size) {
//				// listResult = list.getContent().subList(0, size);
//				// }
//
//				count = (long) userMapper.queryAllCount(begin, end, country, userID, userName, email);
//				listResult = userMapper.queryAll(begin, end, country, userID, userName, email, (page - 1) * size, size);
//
//			} else {
//
//				listResult = userMapper.queryAll(begin, end, country, userID, userName, email, (page - 1) * size, size);
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			return new ReturnResult<List<User>>("001", e.toString(), count, listResult);
//		}
//
//		return new ReturnResult<List<User>>("000", "", count, listResult);
//	}
//}