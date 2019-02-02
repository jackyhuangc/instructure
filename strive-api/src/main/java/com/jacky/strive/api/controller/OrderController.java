package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.KeyValue;
import com.jacky.strive.dao.model.MemberOrder;
import com.jacky.strive.dao.model.MemberOrderInterest;
import com.jacky.strive.dao.model.MemberOrderPay;
import com.jacky.strive.service.OrderService;
import com.jacky.strive.service.dto.MemberOrderExt;
import com.jacky.strive.service.dto.OrderPayDto;
import com.jacky.strive.service.dto.OrderQueryDto;
import com.jacky.strive.service.enums.OrderStatusEnum;
import com.jacky.strive.service.enums.OrderTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qsq.biz.common.util.AssertUtil;
import qsq.biz.common.util.DateUtil;
import qsq.biz.scheduler.entity.ResResult;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/{order_no}")
    public ResResult get(@PathVariable("order_no") String orderNo) {

        MemberOrder order = orderService.findByOrderNo(orderNo);
        AssertUtil.notNull(order, "订单不存在");

        return ResResult.success("", order);
    }

    /**
     * 下单
     *
     * @param order
     * @return
     */
    @PostMapping("/place")
    public ResResult place(@RequestBody MemberOrder order) {

        ResResult resResult = orderService.add(order);

        return resResult;
    }

    /**
     * 批量下单（购物车）
     *
     * @param orderList
     * @return
     */
    @PostMapping("/placeBatch")
    public ResResult placeBatch(@RequestBody List<MemberOrder> orderList) {

        ResResult resResult = orderService.addBatch(orderList);

        return resResult;
    }

    /**
     * 支付链接
     *
     * @param payDto
     * @return
     */
    @PostMapping("/pay")
    public ResResult pay(@RequestBody OrderPayDto payDto) {

        ResResult resResult = orderService.pay(payDto);

        return resResult;
    }

    @GetMapping("/pay/{pay_batch_no}")
    public ResResult getPay(@PathVariable("pay_batch_no") String payBatchNo) {

        MemberOrderPay orderPay = orderService.findByPayBatchNo(payBatchNo);
        AssertUtil.notNull(orderPay, "支付信息不存在");

        return ResResult.success("", orderPay);
    }

    /**
     * 入库
     *
     * @param order
     * @return
     */
    @PostMapping("/stockIn")
    public ResResult stockIn(@RequestBody MemberOrder order) {

        order.setOrderType(OrderTypeEnum.QUOTAS.getValue());
        order.setOrderStatus(OrderStatusEnum.STATUS_REFUNDED.getValue());
        ResResult resResult = orderService.add(order);

        return resResult;
    }

    /**
     * 出库
     *
     * @param order
     * @return
     */
    @PostMapping("/stockOut")
    public ResResult stockOut(@RequestBody MemberOrder order) {

        order.setOrderType(OrderTypeEnum.QUOTAS.getValue());
        order.setOrderStatus(OrderStatusEnum.STATUS_DELIVERED.getValue());
        ResResult resResult = orderService.add(order);

        return resResult;
    }

    // 比如超时未支付，我们可以取消订单
    @PostMapping("/close/{order_no}")
    public ResResult close(@PathVariable("order_no") String orderNo) {
        boolean ret = orderService.close(orderNo);

        return ResResult.success("取消订单成功", ret);
    }

    /**
     * 退款申请，自动审核并退款
     *
     * @param orderNo
     * @return
     */
    @PostMapping("/revoking/{order_no}")
    public ResResult revoking(@PathVariable("order_no") String orderNo) {
        boolean ret = orderService.revoking(orderNo);

        return ResResult.success("退款申请成功", ret);
    }

    /**
     * 发货申请
     *
     * @param orderNo
     * @return
     */
    @PostMapping("/delivering/{order_no}")
    public ResResult delivering(@PathVariable("order_no") String orderNo) {
        boolean ret = orderService.delivering(orderNo);

        return ResResult.success("发货申请成功", ret);
    }

//    // 退款审核并退款，放在退款后处理
//    @PostMapping("/revoke_audit/{order_no}")
//    public ResResult revokeAudit(@PathVariable("order_no") String orderNo,Order order) {
//        return null;
//    }

    // 确认(发货) 特别增加，客户/商家可调用
    @PostMapping("/delivered/{order_no}")
    public ResResult delivered(@PathVariable("order_no") String orderNo, @RequestBody MemberOrder order) {

        order.setOrderNo(orderNo);
        boolean ret = orderService.delivered(order);

        return ResResult.success("发货确认成功", ret);
    }

    // 收货
    @PostMapping("/received/{order_no}")
    public ResResult received(@PathVariable("order_no") String orderNo, @RequestBody MemberOrder order) {

        order.setOrderNo(orderNo);
        boolean ret = orderService.received(order);

        return ResResult.success("收货确认成功", ret);
    }

    // 退货申请
    @PostMapping("/refunding/{order_no}")
    public ResResult refunding(@PathVariable("order_no") String orderNo, @RequestBody MemberOrder order) {

        order.setOrderNo(orderNo);
        boolean ret = orderService.refunding(order);

        return ResResult.success("退货申请成功", ret);
    }

    // 退货审核并退款
    @PostMapping("/refund_audit/{order_no}")
    public ResResult refundAudit(@PathVariable("order_no") String orderNo, @RequestBody MemberOrder order) {

        order.setOrderNo(orderNo);
        boolean ret = orderService.refundAudit(order);

        return ResResult.success("退货确认成功", ret);
    }

    @PostMapping("/query")
    public ResResult query(@RequestBody OrderQueryDto queryDto) {

        PageInfo<MemberOrderExt> productList = orderService.findOrderList(queryDto);

        return ResResult.success("", productList);
    }

    @GetMapping("/query_interest/{order_no}")
    public ResResult queryInterest(@PathVariable("order_no") String orderNo) {

        List<MemberOrderInterest> productList = orderService.findInterestByOrderNo(orderNo);

        return ResResult.success("", productList);
    }

    @GetMapping("/generateNewOrderNo")
    public ResResult generateNewOrderNo() {

        String ret = orderService.generateNewOrderNo("");

        return ResResult.success("生成ID成功", ret);
    }

    @PostMapping("/ordersProductStatistic")
    public ResResult getOrdersProducts(@RequestBody OrderQueryDto queryDto) {

        List<KeyValue> result = orderService.queryGroupBy(queryDto, MemberOrderExt::getOrderProductName);

        return ResResult.success(result);
    }

    @PostMapping("/ordersDeviceStatistic")
    public ResResult getOrdersDevice(@RequestBody OrderQueryDto queryDto) {

        List<KeyValue> result = orderService.queryGroupBy(queryDto, MemberOrderExt::getOrderFrom);

        return ResResult.success(result);
    }

    @PostMapping("/ordersStatistic")
    public ResResult getLastOrdersStatistic(@RequestBody OrderQueryDto queryDto) {

        if (null == queryDto.getBeginDate()) {
            queryDto.setBeginDate(DateUtil.date(DateUtil.addDays(-2)));
        }

        queryDto.setPage(1);
        queryDto.setSize(Integer.MAX_VALUE);

        // 查询
        PageInfo<MemberOrderExt> orderList = orderService.findOrderList(queryDto);

        // 对时间进行更改，清洗为yyyyMMdd(后面按天分组用)
        for (MemberOrderExt detail : orderList.getList()) {

            detail.setCreatedAt(DateUtil.date(detail.getCreatedAt()));
        }

        // 分组
        Map<Date, List<MemberOrderExt>> map = orderList.getList().stream().collect(Collectors.groupingBy(MemberOrderExt::getCreatedAt));

        // 组装结果
        List<KeyValue> result = new ArrayList<>();
        for (Map.Entry<Date, List<MemberOrderExt>> detail : map.entrySet()) {

            KeyValue kv = new KeyValue();
            kv.setKeyvalueKey(DateUtil.formatOnlyDay(detail.getKey()));
            // 下单金额
            kv.setKeyvalueValue(String.valueOf(detail.getValue().stream().map(MemberOrderExt::getPayQuotas).reduce(BigDecimal.ZERO, BigDecimal::add)));
            // 成交金额
            kv.setKeyvalueMemo(String.valueOf(detail.getValue().stream().filter(p -> p.getOrderStatus() > 1).map(MemberOrderExt::getPayQuotas).reduce(BigDecimal.ZERO, BigDecimal::add)));

            result.add(kv);
        }

        result = result.stream().sorted(Comparator.comparing(e -> e.getKeyvalueKey())).collect(Collectors.toList());

        return ResResult.success(result);
    }
}
