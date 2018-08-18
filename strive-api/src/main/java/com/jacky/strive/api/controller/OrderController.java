package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.model.Order;
import com.jacky.strive.dao.model.Product;
import com.jacky.strive.service.OrderService;
import com.jacky.strive.service.dto.OrderQueryDto;
import com.jacky.strive.service.enums.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qsq.biz.common.util.AssertUtil;
import qsq.biz.scheduler.entity.ResResult;

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

        Order o = orderService.findByOrderNo(orderNo);
        AssertUtil.notNull(o, "订单不存在");

        return ResResult.success("", o);
    }

    /**
     * 下单即支付(扣余额)
     *
     * @param order
     * @return
     */
    @PostMapping("/place")
    public ResResult place(@RequestBody Order order) {

        Order o = orderService.add(order);

        AssertUtil.notNull(o, "下单失败");
        return ResResult.success("", o);
    }

//    // 取消订单，目前不支持
//    @PutMapping("/close/{order_no}")
//    public ResResult close(@PathVariable("order_no") String orderNo) {
//        return null;
//    }

//    // 支付，放在下单后自动处理
//    @PostMapping("/pay/{order_no}")
//    public ResResult pay(@PathVariable("order_no") String orderNo) {
//        return null;
//    }

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
    public ResResult delivered(@PathVariable("order_no") String orderNo, @RequestBody Order order) {

        boolean ret = orderService.delivered(order);

        return ResResult.success("发货确认成功", ret);
    }

    // 收货
    @PostMapping("/received/{order_no}")
    public ResResult received(@PathVariable("order_no") String orderNo, @RequestBody Order order) {

        boolean ret = orderService.received(order);

        return ResResult.success("收货确认成功", ret);
    }

    // 退货申请
    @PostMapping("/refunding/{order_no}")
    public ResResult refunding(@PathVariable("order_no") String orderNo, @RequestBody Order order) {

        boolean ret = orderService.refunding(order);

        return ResResult.success("退货申请成功", ret);
    }

    // 退货审核并退款
    @PostMapping("/refund_audit/{order_no}")
    public ResResult refundAudit(@PathVariable("order_no") String orderNo, @RequestBody Order order) {

        boolean ret = orderService.refundAudit(order);

        return ResResult.success("退货确认成功", ret);
    }

    @PostMapping("/query")
    public ResResult query(@RequestBody OrderQueryDto queryDto) {

        PageInfo<Order> productList = orderService.findOrderList(queryDto);

        return ResResult.success("", productList);
    }
}
