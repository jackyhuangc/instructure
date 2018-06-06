package com.jacky.strive.api.controller;

import com.jacky.strive.dao.model.Order;
import com.jacky.strive.service.dto.OrderQueryDto;
import org.springframework.web.bind.annotation.*;
import qsq.biz.scheduler.entity.ResResult;

/**
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/query")
    public ResResult query(OrderQueryDto queryDto) {
        return null;
    }

    @GetMapping("/{order_no}")
    public ResResult get(@PathVariable("order_no") String orderNo) {
        return null;
    }

    // 下单即支付(扣余额)
    @PostMapping("/place")
    public ResResult place(Order order) {
        return null;
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

    // 退款申请，自动审核并退款
    @PostMapping("/revoke")
    public ResResult revoke(Order order) {
        return null;
    }

//    // 退款审核并退款，放在退款后处理
//    @PostMapping("/revoke_audit/{order_no}")
//    public ResResult revokeAudit(@PathVariable("order_no") String orderNo,Order order) {
//        return null;
//    }

    // 确认(发货) 特别增加，客户/商家可调用
    @PostMapping("/confirm/{order_no}")
    public ResResult confirm(@PathVariable("order_no") String orderNo) {
        return null;
    }

    // 发货
    @PostMapping("/deliver/{order_no}")
    public ResResult deliver(@PathVariable("order_no") String orderNo,Order order) {
        return null;
    }

    // 收货
    @PostMapping("/receive/{order_no}")
    public ResResult receive(@PathVariable("order_no") String orderNo,Order order) {
        return null;
    }

    // 退货申请
    @PostMapping("/refund/{order_no}")
    public ResResult refund(@PathVariable("order_no") String orderNo,Order order) {
        return null;
    }

    // 退货审核并退款
    @PostMapping("/refund_audit")
    public ResResult refundAudit(Order order) {
        return null;
    }
}
