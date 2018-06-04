/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/6/2 22:18
 * @since jdk1.8
 */
package com.jacky.strive.api;

/** FIXME 命名规范
 * Dao 接口命名
 *
 * insert
 * batchInsert
 * selectOne
 * selectById
 * count
 * selectList
 * update
 * deleteById
 *
 * Service 接口命名
 *
 * add
 * findById
 * findByXXX
 * findXXXList
 * modify
 * remove
 *
 * Controller 接口命名
 *
 * 普通方式，注重业务特性
 * getXXXX
 * queryXXXX
 * closeXXXX
 * autoPayXXXX
 *
 * restful 风格，注重实体关系
 * GET /tickets # 获取ticket列表
 * GET /tickets/12 # 查看某个具体的ticket
 * POST /tickets # 新建一个ticket
 * PUT /tickets/12 # 更新ticket 12.
 * DELETE /tickets/12 #删除ticekt 12
 *
 * 如何处理关联？关于如何处理资源之间的管理REST原则也有相关的描述：
 * GET /tickets/12/messages- Retrieves list of messages for ticket #12
 * GET /tickets/12/messages/5- Retrieves message #5 for ticket #12
 * POST /tickets/12/messages- Creates a new message in ticket #12
 * PUT /tickets/12/messages/5- Updates message #5 for ticket #12
 * PATCH /tickets/12/messages/5- Partially updates message #5 for ticket #12
 * DELETE /tickets/12/messages/5- Deletes message #5 for ticket #12
 */

/**
 * 15.【参考】各层命名规约：  *
 * A)  Service/DAO层方法命名规约 *
 * 1） 获取单个对象的方法用get做前缀。 *
 * 2） 获取多个对象的方法用list做前缀。 *
 * 3） 获取统计值的方法用count做前缀。 *
 * 4） 插入的方法用save（推荐）或insert做前缀。 *
 * 5） 删除的方法用remove（推荐）或delete做前缀。 *
 * 6） 修改的方法用update做前缀。 *
 * B)  领域模型命名规约 *
 * 1） 数据对象：xxxDO，xxx即为数据表名。 *
 * 2） 数据传输对象：xxxDTO，xxx为业务领域相关的名称。 *
 * 3） 展示对象：xxxVO，xxx一般为网页名称. *
 * 4）  POJO是DO/DTO/BO/VO的统称，禁止命名成xxxPOJO。
 */