package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.KeyValueDao;
import com.jacky.strive.dao.MemberChargeDao;
import com.jacky.strive.dao.MemberLogDao;
import com.jacky.strive.dao.OrderDao;
import com.jacky.strive.dao.model.Member;
import com.jacky.strive.dao.model.MemberCharge;
import com.jacky.strive.dao.model.MemberLog;
import com.jacky.strive.dao.model.Order;
import com.jacky.strive.service.dto.ChargeQueryDto;
import com.jacky.strive.service.dto.OrderQueryDto;
import com.jacky.strive.service.enums.ChargeTypeEnum;
import com.jacky.strive.service.enums.LogBusiEnum;
import com.jacky.strive.service.enums.LogTypeEnum;
import com.jacky.strive.service.enums.OrderStatusEnum;
import org.apache.poi.poifs.crypt.dsig.OOXMLURIDereferencer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qsq.biz.common.util.AssertUtil;
import qsq.biz.common.util.DateUtil;
import qsq.biz.common.util.StringUtil;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author huangchao
 * @create 2018/6/6 上午11:50
 * @desc
 **/
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class OrderService {
    // 是否自动支付
    private static final boolean NEED_AUTOPAY = true;

    // 是否需要确认
    private static final boolean NEED_CONFIRM = true;

    // 是否自动退款
    private static final boolean NEED_REVOKED = true;

    @Autowired
    OrderDao orderDao;

    @Autowired
    MemberLogDao memberLogDao;

    @Autowired
    MemberChargeDao memberChargeDao;

    @Autowired
    KeyValueDao keyValueDao;

    @Autowired
    MemberService memberService;

    @Autowired
    LogService logService;

    @Autowired
    ChargeService chargeService;

    public Order add(Order order) {

        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        order.setOrderStatus(OrderStatusEnum.STATUS_NEW.getValue());
        order.setCreatedAt(DateUtil.now());
        int ret = orderDao.insert(order);
        AssertUtil.isTrue(ret > 0, "下单失败");

        // 是否自动支付，根据KV判断，扣减余额
        if (NEED_AUTOPAY) {

            MemberLog log = new MemberLog();
            log.setCreatedAt(DateUtil.now());
            log.setLogBusi(LogBusiEnum.消费.getValue());

            // 要么用余额，要么用积分，暂时不支持余额+积分
            log.setLogType(order.getOrderAmount() > 0 ?
                    LogTypeEnum.QUOTAS.getValue() : LogTypeEnum.POINTS.getValue());
            log.setMemberNo(order.getMemberNo());

            // 订单实际成交总价+邮费
            Integer quotas = order.getOrderAmount() * order.getOrderPrice() * order.getOrderDiscount() +
                    order.getOrderDeliverFee();
            log.setMemberQuotas(-quotas);

            // 是否赠送积分，跟进商品来
            log.setMemberPoints(order.getOrderPoints());
            log.setUpdatedAt(DateUtil.now());
            log.setLogMemo("");
            log = logService.add(log);
            AssertUtil.notNull(log, "日志添加失败");

            order.setOrderStatus(OrderStatusEnum.STATUS_PAID.getValue());

            if (NEED_CONFIRM) {
                order.setOrderStatus(OrderStatusEnum.STATUS_CONFIRM.getValue());
            }

            boolean blRet = activate(order, OrderStatusEnum.STATUS_NEW.getValue(), order.getOrderStatus());
            AssertUtil.isTrue(blRet, "下单失败");

            // 修改账户余额
            member.setMemberQuotas(member.getMemberQuotas() + log.getMemberQuotas());
            member.setMemberPoints(member.getMemberPoints() + log.getMemberPoints());
            member = memberService.modify(member);
            AssertUtil.notNull(member, "账户修改失败");
        }

        return ret > 0 ? order : null;
    }

    // 退款申请
    public boolean revoking(String orderNo) {
        Order order = findByOrderNo(orderNo);
        AssertUtil.notNull(order, "订单不存在");

        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        // 只有已确认的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_CONFIRM.getValue()), "未确认订单不能申请退款");

        order.setOrderStatus(OrderStatusEnum.STATUS_REVOKING.getValue());

        // 针对自动发货确认=false时，自动处理STATUS_REVOKING订单，进行退款
        // 针对自动发货确认=true时，手动处理STATUS_REVOKING订单，才能退款
        if (NEED_REVOKED) {
            // 自动退款并更改金额
            order.setOrderStatus(OrderStatusEnum.STATUS_REVOKED.getValue());

            MemberLog log = new MemberLog();
            log.setCreatedAt(DateUtil.now());
            log.setLogBusi(LogBusiEnum.消费.getValue());

            // 要么用余额，要么用积分，暂时不支持余额+积分
            log.setLogType(order.getOrderAmount() > 0 ?
                    LogTypeEnum.QUOTAS.getValue() : LogTypeEnum.POINTS.getValue());
            log.setMemberNo(order.getMemberNo());

            // 订单实际成交总价+邮费
            Integer quotas = order.getOrderAmount() * order.getOrderPrice() * order.getOrderDiscount() +
                    order.getOrderDeliverFee();
            log.setMemberQuotas(quotas);

            // 是否赠送积分，跟进商品来
            log.setMemberPoints(-order.getOrderPoints());
            log.setUpdatedAt(DateUtil.now());
            log.setLogMemo("");
            log = logService.add(log);
            AssertUtil.notNull(log, "日志添加失败");

            // 修改账户余额
            member.setMemberQuotas(member.getMemberQuotas() + log.getMemberQuotas());
            member.setMemberPoints(member.getMemberPoints() + log.getMemberPoints());
            member = memberService.modify(member);
            AssertUtil.notNull(member, "账户修改失败");
        }

        boolean ret = activate(order, OrderStatusEnum.STATUS_CONFIRM.getValue(), order.getOrderStatus());
        AssertUtil.isTrue(ret, "退款申请失败");

        return ret;
    }

    // 发货申请
    public boolean delivering(String orderNo) {

        Order order = findByOrderNo(orderNo);
        AssertUtil.notNull(order, "订单不存在");

        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        // 只有已确认的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_CONFIRM.getValue()), "未确认订单不能申请发货");

        order.setOrderStatus(OrderStatusEnum.STATUS_DELIVERING.getValue());

        boolean ret = activate(order, OrderStatusEnum.STATUS_CONFIRM.getValue(), order.getOrderStatus());
        AssertUtil.isTrue(ret, "退款申请失败");

        return ret;
    }

    // 发货确认
    public boolean delivered(Order order) {
        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        Order o = findByOrderNo(order.getOrderNo());
        AssertUtil.notNull(o, "订单不存在");

        // 只有已确认的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_DELIVERING.getValue()), "非代发货订单不能确认发货");

        o.setOrderStatus(OrderStatusEnum.STATUS_DELIVERED.getValue());
        o.setDeliverAt(DateUtil.now());
        o.setDeliverMemo(order.getDeliverMemo());
        boolean ret = activate(o, OrderStatusEnum.STATUS_DELIVERING.getValue(), o.getOrderStatus());
        AssertUtil.isTrue(ret, "发货申请失败");

        return ret;
    }

    public boolean activate(Order order, Integer oriStatus, Integer newStatus) {

        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNo", order.getOrderNo());
        criteria.andEqualTo("orderStatus", oriStatus);

        order.setOrderStatus(newStatus);

        int ret = orderDao.updateByExampleSelective(order, example);
        return ret > 0;
    }

    public Order findByOrderNo(String orderNo) {
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNo", orderNo);

        Order order = orderDao.selectOneByExample(example);

        return order;
    }

    public PageInfo<Order> findOrderList(OrderQueryDto queryDto) {

        Page<Member> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(Order.class);
        Example.Criteria criteria1 = example.createCriteria();

        if (null == queryDto.getOrderType()) {
            criteria1.andIsNull("orderType");
        } else {
            criteria1.andEqualTo("orderType", queryDto.getOrderType());
        }

        if (null == queryDto.getOrderStatus()) {
            criteria1.andIsNull("orderStatus");
        } else {
            criteria1.andEqualTo("orderStatus", queryDto.getOrderStatus());
        }

        Example.Criteria criteria2 = example.createCriteria();

        String condition = "%%";
        if (null != queryDto.getMemberNo()) {
            condition = "%" + queryDto.getMemberNo() + "%";
        }

        criteria2.andLike("memberNo", condition);
        criteria2.orLike("productName", condition);

        example.and(criteria2);
        example.setOrderByClause("created_at desc");
        List<Order> orderList = orderDao.selectByExample(example);

        PageInfo<Order> pageInfo = new PageInfo<>(orderList);

        return pageInfo;
    }

    public String generateNewOrderNo() {

        String maxOrderNo = keyValueDao.getDynamicResult("SELECT max(order_no) FROM `order`");

        if (!StringUtil.isEmtpy(maxOrderNo)) {
            maxOrderNo = "P" + String.format("%08d", Integer.valueOf(Integer.parseInt(maxOrderNo.substring(1)) + 1));
        } else {
            maxOrderNo = "P00000001";
        }
        return maxOrderNo;
    }
}