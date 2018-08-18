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
import com.jacky.strive.service.enums.*;
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

            order.setOrderStatus(OrderStatusEnum.STATUS_PAID.getValue());

            if (NEED_CONFIRM) {
                order.setOrderStatus(OrderStatusEnum.STATUS_CONFIRM.getValue());
            }
        }

        boolean blRet = activate(order, OrderStatusEnum.STATUS_NEW.getValue(), order.getOrderStatus());
        AssertUtil.isTrue(blRet, "下单失败");

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

        if (NEED_REVOKED) {
            // 自动退款并更改金额
            order.setOrderStatus(OrderStatusEnum.STATUS_REVOKED.getValue());
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

    // 收货确认
    public boolean received(Order order) {
        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        Order o = findByOrderNo(order.getOrderNo());
        AssertUtil.notNull(o, "订单不存在");

        // 只有已发货的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_DELIVERED.getValue()), "非已发货订单不能确认收货");

        o.setOrderStatus(OrderStatusEnum.STATUS_RECEIVED.getValue());
        o.setFinishAt(DateUtil.now());
        o.setFinishMemo(order.getFinishMemo());
        boolean ret = activate(o, OrderStatusEnum.STATUS_DELIVERED.getValue(), o.getOrderStatus());
        AssertUtil.isTrue(ret, "发货申请失败");

        return ret;
    }

    // 申请退货
    public boolean refunding(Order order) {
        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        Order o = findByOrderNo(order.getOrderNo());
        AssertUtil.notNull(o, "订单不存在");

        // 只有已发货的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_DELIVERED.getValue()), "非已发货订单不能确认收货");

        o.setOrderStatus(OrderStatusEnum.STATUS_REFUNDING.getValue());
        // o.setFinishAt(DateUtil.now());
        o.setFinishMemo(order.getFinishMemo());
        boolean ret = activate(o, OrderStatusEnum.STATUS_DELIVERED.getValue(), o.getOrderStatus());
        AssertUtil.isTrue(ret, "发货申请失败");

        return ret;
    }

    // 退货完成
    public boolean refundAudit(Order order) {
        Order o = findByOrderNo(order.getOrderNo());
        AssertUtil.notNull(o, "订单不存在");

        // 只有已发货的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_REFUNDING.getValue()), "非已发货订单不能确认收货");

        o.setOrderStatus(order.getOrderStatus());

        if (OrderStatusEnum.STATUS_REFUNDED.getValue().equals(order.getOrderStatus())) {
            // 退货完成，自动退款
            o.setOrderStatus(OrderStatusEnum.STATUS_REVOKED.getValue());
        }

        o.setFinishAt(DateUtil.now());
        o.setFinishMemo(order.getFinishMemo());
        boolean ret = activate(o, OrderStatusEnum.STATUS_REFUNDING.getValue(), o.getOrderStatus());
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