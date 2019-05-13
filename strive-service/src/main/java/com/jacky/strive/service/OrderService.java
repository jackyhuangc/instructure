package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.common.*;
import com.jacky.strive.common.entity.ResResult;
import com.jacky.strive.dao.*;
import com.jacky.strive.dao.model.*;
import com.jacky.strive.service.dto.KvOrderConfig;
import com.jacky.strive.service.dto.MemberOrderExt;
import com.jacky.strive.service.dto.OrderPayDto;
import com.jacky.strive.service.dto.OrderQueryDto;
import com.jacky.strive.service.enums.*;
import com.jacky.strive.service.utils.WXRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    ProductService productService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    KeyValueService keyValueService;

    @Autowired
    OrderPayDao orderPayDao;

    @Autowired
    OrderInterestDao orderInterestDao;

    @Autowired
    ChargeService chargeService;

    public ResResult add(MemberOrder order) {

        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        Product product = productService.findByProductNo(order.getOrderProductNo());
        AssertUtil.notNull(product, "商品不存在");

        if (StringUtil.isEmtpy(order.getOrderNo())) {
            order.setOrderNo(generateNewOrderNo(""));
        }

        order.setOrderAwardQuotas(BigDecimal.ZERO);
        // 本版本不使用折扣率，折扣率用来保存计息费率
        //order.setOrderDiscount(product.getProductDiscountRate());
        order.setOrderDiscount(BigDecimal.ZERO);
        order.setDeliverMemo(product.getProductDiscountRate().toString());
        order.setOrderDeliverFee(product.getProductDeliverFee());

        if (product.getProductExchange() > 0) {

            // 积分消费
            order.setOrderPoints(-product.getProductExchange());
            AssertUtil.isTrue(member.getMemberPoints() + order.getOrderPoints() > 0, "您的积分余额不足");

        } else {
            // 积分赠送
            order.setOrderPoints(product.getProductPoints());
        }

        order.setOpenId(member.getOpenId());

        if (!StringUtil.isEmtpy(order.getOrderVoucherNo())) {
            MemberVoucher voucher = voucherService.findByVoucherNo(order.getOrderVoucherNo());
            AssertUtil.notNull(voucher, "代金券不存在");

            order.setOrderVoucher(voucher.getVoucherQuotas());
        } else {
            order.setOrderVoucher(0);
        }

        if (null == order.getOrderStatus()) {
            order.setOrderStatus(OrderStatusEnum.STATUS_NEW.getValue());
        }

        order.setCreatedAt(DateUtil.now());

        Double quotas = (order.getOrderPrice().doubleValue() * order.getOrderAmount().doubleValue()) * (1 - order.getOrderDiscount().doubleValue()) + order.getOrderDeliverFee() - order.getOrderVoucher();
        order.setPayQuotas(BigDecimal.valueOf(quotas));


        if ("Balance".equals(order.getPayType())) {

            // 余额支付，直接设置为已支付状态
            order.setOrderStatus(OrderStatusEnum.STATUS_PAID.getValue());
            order.setPayAt(DateUtil.now());

            KeyValue kv = keyValueService.findByKey("KvOrderConfig");

            if (null != kv) {
                KvOrderConfig config = JsonUtil.fromJson(kv.getKeyvalueValue(), KvOrderConfig.class);
                // 是否设置为待确认状态
                if (config.isAutoConfirm()) {
                    order.setOrderStatus(OrderStatusEnum.STATUS_CONFIRM.getValue());
                }
            }
        } else if (!StringUtil.isEmtpy(order.getPayType()) && StringUtil.isEmtpy(order.getPayBatchNo())) {
            // 非余额支付需要批次号
            // 重新生成批次号
            MemberOrderPay orderPay = new MemberOrderPay();
            orderPay.setPayBatchNo(generateNewPayNo());
            orderPay.setPayStatus(2);
            orderPay.setCreatedAt(DateUtil.now());
            orderPay.setMemberNo(order.getMemberNo());
            orderPay.setOpenId(order.getOpenId());
            orderPay.setPayType(order.getPayType());
            orderPay.setPayQuotas(order.getPayQuotas());
            orderPay.setPayPoints(order.getOrderPoints());
            orderPay.setPayMemo(order.getOrderProductName());
            order.setPayBatchNo(orderPay.getPayBatchNo());
            int ret = orderPayDao.insert(orderPay);
            AssertUtil.isTrue(ret > 0, "生成支付信息失败");
        }

        int ret = orderDao.insert(order);
        AssertUtil.isTrue(ret > 0, "下单失败");

        return ret > 0 ? ResResult.success("交易成功", order) : ResResult.fail("交易失败");
    }

    public ResResult addBatch(List<MemberOrder> orderList) {

        MemberOrderPay orderPay = new MemberOrderPay();
        orderPay.setPayBatchNo(generateNewPayNo());
        orderPay.setPayQuotas(BigDecimal.ZERO);
        orderPay.setPayPoints(0);
        orderPay.setPayStatus(2);
        orderPay.setCreatedAt(DateUtil.now());

        orderList.stream().forEach(order -> {

            // 非余额支付需要批次号
            if (!StringUtil.isEmtpy(order.getPayType()) && !"Balance".equals(order.getPayType())) {

                order.setPayBatchNo(orderPay.getPayBatchNo());
                ResResult resResult = add(order);
                AssertUtil.isTrue(resResult.isSuccess(), "下单失败");

                Double quotas = (order.getOrderPrice().doubleValue() * order.getOrderAmount().doubleValue()) * (1 - order.getOrderDiscount().doubleValue()) + order.getOrderDeliverFee() - order.getOrderVoucher();
                orderPay.setMemberNo(order.getMemberNo());
                orderPay.setOpenId(order.getOpenId());
                orderPay.setPayType(order.getPayType());
                orderPay.setPayQuotas(BigDecimal.valueOf(orderPay.getPayQuotas().doubleValue() + quotas));
                orderPay.setPayPoints(order.getOrderPoints());
                if (StringUtil.isEmtpy(orderPay.getPayMemo())) {
                    orderPay.setPayMemo(order.getOrderProductName());
                } else {
                    orderPay.setPayMemo(orderList.get(0).getOrderProductName() + "等");
                }

            } else {
                ResResult resResult = add(order);
                AssertUtil.isTrue(resResult.isSuccess(), "下单失败");
            }
        });

        if (!StringUtil.isEmtpy(orderList.get(0).getPayBatchNo())) {
            int ret = orderPayDao.insert(orderPay);
            AssertUtil.isTrue(ret > 0, "生成支付信息失败");
        }

        return ResResult.success("交易成功", orderPay);
    }

    public ResResult pay(OrderPayDto payDto) {

        Example example = new Example(MemberOrderPay.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("payBatchNo", payDto.getPayBatchNo());
        criteria.andEqualTo("memberNo", payDto.getMemberNo());

        MemberOrderPay orderPay = orderPayDao.selectOneByExample(example);
        AssertUtil.notNull(orderPay, "支付信息不存在");
        AssertUtil.isTrue(2 == orderPay.getPayStatus(), "该单已支付");

        Member member = memberService.findByMemberNo(payDto.getMemberNo());
        AssertUtil.notNull(member, "会员不存在");

        AssertUtil.isTrue(member.getMemberPoints() + orderPay.getPayPoints() > 0, "您的积分余额不足");

        AssertUtil.isTrue(!StringUtil.isEmtpy(orderPay.getPayType()) && orderPay.getPayType().indexOf("WX_") >= -1, "目前仅支持微信支付");

        return WXRequestUtil.PrePayment(payDto.getPayBatchNo(), orderPay.getPayQuotas(), orderPay.getPayType().substring(3), orderPay.getOpenId(), orderPay.getPayMemo());
    }

    // 退款申请
    public boolean revoking(String orderNo) {
        MemberOrder order = findByOrderNo(orderNo);
        AssertUtil.notNull(order, "订单不存在");

        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        // 只有已支付或已确认的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_PAID.getValue()) || order.getOrderStatus().equals(OrderStatusEnum.STATUS_CONFIRM.getValue()), "未确认订单不能申请退款");

        Integer status = OrderStatusEnum.STATUS_REVOKING.getValue();

        if ("Balance".equals(order.getPayType())) {
            KeyValue kv = keyValueService.findByKey("KvOrderConfig");
            AssertUtil.notNull(kv, "KvOrderConfig不存在");

            KvOrderConfig config = JsonUtil.fromJson(kv.getKeyvalueValue(), KvOrderConfig.class);

            // 自动退款并更改金额
            if (null != config && config.isAutoRevoked()) {
                // 退货完成，自动退款
                if (!StringUtil.isEmtpy(order.getPayType()) && !order.getPayType().equals("Balance")) {
                    status = OrderStatusEnum.STATUS_REVOKING.getValue();
                } else {
                    status = OrderStatusEnum.STATUS_REVOKED.getValue();
                }
            }
        }

        boolean ret = updateStatus(order, order.getOrderStatus(), status);
        AssertUtil.isTrue(ret, "退款申请失败");

        return ret;
    }

    // 发货申请(特殊需求)
    public boolean delivering(String orderNo) {

        MemberOrder order = findByOrderNo(orderNo);
        AssertUtil.notNull(order, "订单不存在");

        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        // 只有已确认的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_CONFIRM.getValue()), "未确认订单不能申请发货");

        order.setOrderStatus(OrderStatusEnum.STATUS_DELIVERING.getValue());

        boolean ret = updateStatus(order, OrderStatusEnum.STATUS_CONFIRM.getValue(), order.getOrderStatus());
        AssertUtil.isTrue(ret, "发货申请失败");

        return ret;
    }

    // 发货确认
    public boolean delivered(MemberOrder order) {
        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        MemberOrder o = findByOrderNo(order.getOrderNo());
        AssertUtil.notNull(o, "订单不存在");

        // 只有已确认的订单才能申请退款
        AssertUtil.isTrue(o.getOrderStatus().equals(OrderStatusEnum.STATUS_PAID.getValue()) || o.getOrderStatus().equals(OrderStatusEnum.STATUS_DELIVERING.getValue()), "非待发货订单不能确认发货");

        // o.setOrderStatus(OrderStatusEnum.STATUS_DELIVERED.getValue());
        o.setDeliverAt(DateUtil.now());
        o.setDeliverMemo(order.getDeliverMemo());
        boolean ret = updateStatus(o, o.getOrderStatus(), OrderStatusEnum.STATUS_DELIVERED.getValue());

        AssertUtil.isTrue(ret, "发货申请失败");

        return ret;
    }

    // 收货确认
    public boolean received(MemberOrder order) {
        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        MemberOrder o = findByOrderNo(order.getOrderNo());
        AssertUtil.notNull(o, "订单不存在");

        // 只有已发货的订单才能申请退款
        AssertUtil.isTrue(o.getOrderStatus().equals(OrderStatusEnum.STATUS_DELIVERED.getValue()), "非已发货订单不能确认收货");

        o.setOrderStatus(OrderStatusEnum.STATUS_RECEIVED.getValue());
        o.setFinishAt(DateUtil.now());
        o.setFinishMemo(order.getFinishMemo());
        o.setOrderOpinionRate(order.getOrderOpinionRate());
        boolean ret = updateStatus(o, OrderStatusEnum.STATUS_DELIVERED.getValue(), o.getOrderStatus());
        AssertUtil.isTrue(ret, "发货申请失败");

        return ret;
    }

    // 申请退货
    public boolean refunding(MemberOrder order) {
        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        MemberOrder o = findByOrderNo(order.getOrderNo());
        AssertUtil.notNull(o, "订单不存在");

        // 只有已发货的订单才能申请退款
        AssertUtil.isTrue(o.getOrderStatus().equals(OrderStatusEnum.STATUS_DELIVERED.getValue()), "非已发货订单不能确认收货");

        o.setOrderStatus(OrderStatusEnum.STATUS_REFUNDING.getValue());
        // o.setFinishAt(DateUtil.now());
        o.setFinishMemo(order.getFinishMemo());
        boolean ret = updateStatus(o, OrderStatusEnum.STATUS_DELIVERED.getValue(), o.getOrderStatus());
        AssertUtil.isTrue(ret, "发货申请失败");

        return ret;
    }

    // 退货完成
    public boolean refundAudit(MemberOrder order) {
        MemberOrder o = findByOrderNo(order.getOrderNo());
        AssertUtil.notNull(o, "订单不存在");

        // 只有已发货的订单才能申请退款
        AssertUtil.isTrue(o.getOrderStatus().equals(OrderStatusEnum.STATUS_REFUNDING.getValue()), "非已发货订单不能确认收货");

        o.setOrderStatus(order.getOrderStatus());

        if (OrderStatusEnum.STATUS_REFUNDED.getValue().equals(order.getOrderStatus())) {
            // 退货完成，余额支付自动退款
            if ("Balance".equals(order.getPayType())) {
                o.setOrderStatus(OrderStatusEnum.STATUS_REVOKED.getValue());
            } else {
                o.setOrderStatus(OrderStatusEnum.STATUS_REVOKING.getValue());
            }
        }

        o.setFinishAt(DateUtil.now());
        o.setFinishMemo(order.getFinishMemo());
        boolean ret = updateStatus(o, OrderStatusEnum.STATUS_REFUNDING.getValue(), o.getOrderStatus());
        AssertUtil.isTrue(ret, "发货申请失败");

        return ret;
    }

    // 取消订单（没支付之前可以取消）
    public boolean close(String orderNo) {
        MemberOrder order = findByOrderNo(orderNo);
        AssertUtil.notNull(order, "订单不存在");

        Member member = memberService.findByMemberNo(order.getMemberNo());
        AssertUtil.notNull(member, "用户不存在");

        // 只有已确认的订单才能申请退款
        AssertUtil.isTrue(order.getOrderStatus().equals(OrderStatusEnum.STATUS_NEW.getValue()), "非未支付订单不能取消");

        // 非余额支付订单在取消前先主动查一次
        if (!StringUtil.isEmtpy(order.getPayType()) && !order.getPayType().equals("Balance")) {
            payQuery(order);
            order = findByOrderNo(orderNo);
        }

        order.setOrderStatus(OrderStatusEnum.STATUS_CLOSED.getValue());
        order.setFinishAt(DateUtil.now());
        order.setFinishMemo("取消订单");

        boolean ret = updateStatus(order, OrderStatusEnum.STATUS_NEW.getValue(), order.getOrderStatus());
        AssertUtil.isTrue(ret, "订单取消失败");

        return ret;
    }

    public boolean updateStatus(MemberOrder order, Integer oriStatus, Integer newStatus) {

        Example example = new Example(MemberOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNo", order.getOrderNo());
        criteria.andEqualTo("orderStatus", oriStatus);

        order.setOrderStatus(newStatus);

        int ret = orderDao.updateByExampleSelective(order, example);
        return ret > 0;
    }

    public MemberOrder findByOrderNo(String orderNo) {
        Example example = new Example(MemberOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNo", orderNo);

        MemberOrder order = orderDao.selectOneByExample(example);
        if (!StringUtil.isEmtpy(order.getPayType()) && !order.getPayType().equals("Balance")) {

            if (order.getOrderStatus().equals(OrderStatusEnum.STATUS_NEW.getValue())) {
                // 待支付订单，都主动再查一把，更新下状态
                payQuery(order);
                order = orderDao.selectOneByExample(example);
            } else if (order.getOrderStatus().equals(OrderStatusEnum.STATUS_REVOKING.getValue())) {

                refundQuery(order);
                order = orderDao.selectOneByExample(example);
            }
        }

        return order;
    }

    /**
     * 订单批量处理
     *
     * @return
     */
    public ResResult batchOrderHandle() {

        Example example = new Example(MemberOrder.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("orderStatus", OrderStatusEnum.STATUS_NEW);
        criteria.orEqualTo("orderStatus", OrderStatusEnum.STATUS_REVOKING);

        example.setOrderByClause("created_at desc");
        List<MemberOrder> orderList = orderDao.selectByExample(example);

        orderList.stream().forEach(order -> {

            if (!StringUtil.isEmtpy(order.getPayType()) && !order.getPayType().equals("Balance")) {

                if (order.getOrderStatus().equals(OrderStatusEnum.STATUS_NEW.getValue())) {
                    // 待支付订单，都主动再查一把，更新下状态
                    payQuery(order);
                    //order = orderDao.selectOneByExample(example);
                } else if (order.getOrderStatus().equals(OrderStatusEnum.STATUS_REVOKING.getValue())) {

                    refundQuery(order);
                    //order = orderDao.selectOneByExample(example);
                }
            }
        });

        return ResResult.success();
    }

    public PageInfo<MemberOrderExt> findOrderList(OrderQueryDto queryDto) {

        Page<Member> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(MemberOrder.class);
        Example.Criteria criteria1 = example.createCriteria();

        if (null != queryDto.getOrderType()) {
            criteria1.andEqualTo("orderType", queryDto.getOrderType());
        }

        if (null != queryDto.getOrderStatus()) {
            criteria1.andIn("orderStatus", Arrays.asList(queryDto.getOrderStatus().split(",")));
        }

        if (null != queryDto.getBeginDate()) {
            criteria1.andGreaterThan("createdAt", queryDto.getBeginDate());
        }

        Example.Criteria criteria2 = example.createCriteria();

        String condition = "%%";
        if (null != queryDto.getOrderNo()) {
            condition = "%" + queryDto.getOrderNo() + "%";
        }

        criteria2.andLike("orderNo", condition);
        criteria2.orLike("memberNo", condition);
        criteria2.orLike("orderProductNo", condition);

        example.and(criteria2);
        example.setOrderByClause("created_at desc");
        List<MemberOrder> orderList = orderDao.selectByExample(example);
        List<MemberOrderExt> result = BeanUtil.map(orderList, MemberOrderExt.class);

        if (queryDto.isIncludeImage() && null != result) {
            result.stream().forEach(order -> {

                Product product = productService.findByProductNo(order.getOrderProductNo());
                if (null != product) {
                    order.setProductImage(product.getProductImage());
                }
            });
        }

        PageInfo<MemberOrderExt> pageInfo = new PageInfo<>(result);

        return pageInfo;
    }

    public List<MemberOrder> findOrderListForInterest(OrderQueryDto queryDto) {

        Page<Member> page = PageHelper.startPage(queryDto.getPage(), queryDto.getSize());

        Example example = new Example(MemberOrder.class);
        Example.Criteria criteria = example.createCriteria();

        if (null != queryDto.getOrderType()) {
            criteria.andEqualTo("orderType", queryDto.getOrderType());
        }

        if (null != queryDto.getOrderStatus()) {
            criteria.andIn("orderStatus", Arrays.asList(queryDto.getOrderStatus().split(",")));
        }

        if (null != queryDto.getBeginDate()) {
            criteria.andGreaterThan("createdAt", queryDto.getBeginDate());
        }

        example.setOrderByClause("created_at desc");
        List<MemberOrder> orderList = orderDao.selectByExample(example);

        return orderList;
    }

    public ResResult payQuery(MemberOrder order) {

        try {

            if (!StringUtil.isEmtpy(order.getPayType()) && !order.getPayType().equals("Balance") && order.getOrderStatus().equals(OrderStatusEnum.STATUS_NEW.getValue())) {

                if (order.getPayType().equals("WX_")) {
                    ResResult resResult = WXRequestUtil.PaymentQuery(order.getPayBatchNo());
                    if (resResult.isSuccess()) {
                        order.setOrderStatus(OrderStatusEnum.STATUS_PAID.getValue());
                        order.setPayAt(DateUtil.now());
                        boolean ret = updateStatus(order, OrderStatusEnum.STATUS_NEW.getValue(), OrderStatusEnum.STATUS_PAID.getValue());
                        AssertUtil.isTrue(ret, "支付状态更新失败");

                        return resResult;
                    } else if (resResult.isFail()) {
                        order.setOrderStatus(OrderStatusEnum.STATUS_CLOSED.getValue());
                        order.setFinishAt(DateUtil.now());
                        order.setFinishMemo(resResult.getMessage());

                        boolean ret = updateStatus(order, OrderStatusEnum.STATUS_NEW.getValue(), OrderStatusEnum.STATUS_CLOSED.getValue());
                        AssertUtil.isTrue(ret, "支付状态更新失败");

                        return resResult;
                    }
                }
            }

            return ResResult.fail("非在线支付无需查询", null);

        } catch (Exception e) {
            return ResResult.process("处理中", null);
        }
    }

    public ResResult refundQuery(MemberOrder order) {

        try {

            if (!StringUtil.isEmtpy(order.getPayType()) && !order.getPayType().equals("Balance") && order.getOrderStatus().equals(OrderStatusEnum.STATUS_REVOKING.getValue())) {

                if (order.getPayType().equals("WX_")) {

                    ResResult resResult = WXRequestUtil.RefundQuery(order.getPayBatchNo(), order.getOrderNo());
                    if (resResult.isSuccess()) {

                        // 退款成功，更改订单状态
                        order.setOrderStatus(OrderStatusEnum.STATUS_REVOKED.getValue());
                        order.setFinishAt(DateUtil.now());
                        boolean ret = updateStatus(order, OrderStatusEnum.STATUS_REVOKING.getValue(), OrderStatusEnum.STATUS_REVOKED.getValue());
                        AssertUtil.isTrue(ret, "支付状态更新失败");

                        return resResult;
                    } else if (resResult.isFail()) {

                        // 退款失败，手动确认
                        // 如果没退款，则重新发起一次退款补单
                        if (resResult.getMessage().equals("REFUNDNOTEXIST")) {

                            Double quotas = (order.getOrderPrice().doubleValue() * order.getOrderAmount().doubleValue()) * (1 - order.getOrderDiscount().doubleValue()) + order.getOrderDeliverFee() - order.getOrderVoucher();
                            // 只支持逐笔退
                            WXRequestUtil.Refund(order.getPayBatchNo(), order.getOrderNo(), BigDecimal.valueOf(quotas), BigDecimal.valueOf(quotas));
                        }
                    }
                }
            }

            return ResResult.fail("非在线支付无需退款查询", null);

        } catch (Exception e) {
            return ResResult.process("处理中", null);
        }
    }

    public MemberOrderPay findByPayBatchNo(String payBatchNo) {
        Example example = new Example(MemberOrderPay.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("payBatchNo", payBatchNo);

        MemberOrderPay orderPay = orderPayDao.selectOneByExample(example);
        AssertUtil.notNull(orderPay, "支付信息不存在");

        return orderPay;
    }

    public List<KeyValue> queryGroupBy(OrderQueryDto queryDto, Function<? super MemberOrderExt, ? extends String> classifier) {
        if (null == queryDto.getBeginDate()) {
            queryDto.setBeginDate(DateUtil.today());
        }

        queryDto.setPage(1);
        queryDto.setSize(Integer.MAX_VALUE);

        // 查询
        PageInfo<MemberOrderExt> orderList = findOrderList(queryDto);

        // 分组
        Map<String, List<MemberOrderExt>> map = orderList.getList().stream().collect(Collectors.groupingBy(classifier));

        // 组装结果
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (Map.Entry<String, List<MemberOrderExt>> detail : map.entrySet()) {
            hashMap.put(detail.getKey(), detail.getValue().size());
        }

        // 排序
        Stream<Map.Entry<String, Integer>> st = hashMap.entrySet().stream();
        List<KeyValue> result = new ArrayList<>();

        st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e ->
                {
                    KeyValue kv = new KeyValue();
                    kv.setKeyvalueKey(e.getKey());
                    kv.setKeyvalueValue(String.valueOf(e.getValue()));
                    result.add(kv);
                }
        );

        return result;
    }

    public MemberOrderInterest addInterest(MemberOrderInterest interest) {
        int ret = orderInterestDao.insert(interest);
        return ret > 0 ? interest : null;
    }

    public boolean delInterest(MemberOrderInterest interest) {
        Example example = new Example(MemberOrderInterest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("interestId", interest.getInterestId());

        int ret = orderInterestDao.deleteByExample(example);
        return ret > 0;
    }

    public List<MemberOrderInterest> findInterestByOrderNo(String orderNo) {
        Example example = new Example(MemberOrderInterest.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderNo", orderNo);

        List<MemberOrderInterest> orderInterestList = orderInterestDao.selectByExample(example);

        return orderInterestList;
    }

    public String generateNewOrderNo(String memberNo) {

        return StringUtil.getUniqueString(memberNo);
    }

    private String generateNewPayNo() {

        String maxPayNo = keyValueDao.getDynamicResult("SELECT max(pay_batch_no) FROM `member_order_pay`");

        if (!StringUtil.isEmtpy(maxPayNo)) {
            maxPayNo = "X" + String.format("%08d", Integer.valueOf(Integer.parseInt(maxPayNo.substring(1)) + 1));
        } else {
            maxPayNo = "X00000001";
        }
        return maxPayNo;
    }
}