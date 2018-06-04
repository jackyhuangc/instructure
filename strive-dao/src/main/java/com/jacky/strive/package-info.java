/**
 * FIXME 将DAO层拆到独立的模块/包后，会提示 No MyBatis mapper was found in / Skipping MapperFactoryBean with name等警告
 *
 * @author Jacky Huang
 * @date 2018/5/22 18:19
 * @since jdk1.8
 */
package com.jacky.strive;
// FIXME tk mybatis 通常使用的情况如下：
// FIXME 1.tk mybatis 应使用在同一个模块中，否则会扫描同项目下其他模块内的包。（报No MyBatis mapper was found in 警告）
// FIXME 2.tk mybatis 不能扫描Mapper<T>, MySqlMapper<T>接口，否则会出错。