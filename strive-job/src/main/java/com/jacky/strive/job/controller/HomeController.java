package com.jacky.strive.job.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.common.entity.result.ResResult;
import com.jacky.common.util.*;
import com.jacky.strive.dao.model.*;
import com.jacky.strive.service.*;
import com.jacky.strive.service.dto.ChargeQueryDto;
import com.jacky.strive.service.dto.MemberQueryDto;
import com.jacky.strive.service.dto.OrderQueryDto;
import com.jacky.strive.service.enums.ChargeStatusEnum;
import com.jacky.strive.service.enums.ChargeTypeEnum;
import com.jacky.strive.service.enums.OrderStatusEnum;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jacky.common.*;

import java.io.Reader;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/17 18:44
 * @since jdk1.8
 */
@RestController
@RequestMapping("/job")
public class HomeController {
    @Autowired
    MemberService memberService;

    @Autowired
    ChargeService chargeService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    KeyValueService keyValueService;

    @GetMapping("/index")
    public String index() {

        return "Hello, xxl-job-strive executor!";
    }

    @GetMapping("/quatos/sync")
    private String syncQuatos() {

        try {
            MemberQueryDto queryDto = new MemberQueryDto();
            PageInfo<Member> memberPageInfo = memberService.findMemberList(queryDto);

            if (null == memberPageInfo || CollectionUtils.isEmpty(memberPageInfo.getList())) {
                LogUtil.warn(String.format("无需要同步的账户数据"));
                return "";
            }

            // 循环同步
            memberPageInfo.getList().forEach(member -> {

                updateInvestor(member);
            });

            return String.format("已处理%s条数据", memberPageInfo.getList().size());

        } catch (Exception e) {
            LogUtil.error(e);
            return e.getMessage();
        }
    }

    @GetMapping("/charge/sync")
    public String syncCharge1() {

        try {

            // 查询需要同步的订单，如3、交易成功
            // AuditState 0、待审核，1、审核通过，2、审核驳回，3、交易成功，4、交易失败，5、交易处理中
            List<_ApplyHistory> historyList = getApplyHistoryByStatus("3");

            if (null == historyList || CollectionUtils.isEmpty(historyList)) {
                LogUtil.warn(String.format("无需要同步的充值提现数据"));
                return "";
            }

            // 循环同步
            historyList.forEach((history) -> {

                // 判断是否已经同步过来
                String chargeNo = history.getInvestorID() + DateUtil.format(history.getApplyTime(), "yyMMddHHmm") + history.getAPID();
                MemberCharge memberCharge = chargeService.findByChargeNo(chargeNo);

                if (null == memberCharge) {

                    Member member = memberService.findByUnionId(history.getInvestorID());
                    if (null != member) {

                        // 没有再添加
                        memberCharge = new MemberCharge();
                        memberCharge.setMemberNo(member.getMemberNo());
                        if (history.getApplyType() == 0) {
                            memberCharge.setChargeType(ChargeTypeEnum.CHARGE.getValue());
                        } else {
                            memberCharge.setChargeType(ChargeTypeEnum.WITHDRAW.getValue());
                        }

                        memberCharge.setChargeStatus(ChargeStatusEnum.STATUS_SUCCESS.getValue());
                        memberCharge.setUpdatedAt(DateUtil.now());
                        memberCharge.setCreatedAt(history.getApplyTime());
                        memberCharge.setChargeNo(chargeNo);
                        memberCharge.setCardNum("");
                        memberCharge.setChargeQuotas(history.getApplyAmount());
                        memberCharge.setChargePoints(0);
                        memberCharge.setPayType("Balance");
                        memberCharge.setChargeMemo(history.getAuditRemark());

                        ResResult resResult = chargeService.add(memberCharge);
                        if (resResult.isSuccess()) {
                            history.setAuditRemark(history.getAuditRemark() + "-已处理");
                            updateApplyHistory(history);
                            LogUtil.info(String.format("订单%s同步成功", chargeNo));
                        }
                    } else {
                        LogUtil.info(String.format("用户%s不存在", history.getInvestorID()));
                    }
                } else {
                    LogUtil.info(String.format("订单%s已经存在，不能重复同步", chargeNo));
                }
            });

            return String.format("已处理%s条数据", historyList.size());
        } catch (Exception e) {
            LogUtil.error(e);
            return e.getMessage();
        }
    }

    //@GetMapping("/charge/sync")
    public String syncCharge() {

        try {

            // 查询需要同步的订单，如处理中  1、审核通过
            // AuditState 0、待审核，1、审核通过，2、审核驳回，3、交易成功，4、交易失败，5、交易处理中
            List<_ApplyHistory> historyList = getApplyHistoryByStatus("1,5");

            if (null == historyList || CollectionUtils.isEmpty(historyList)) {
                LogUtil.warn(String.format("无需要同步的充值提现数据"));
                return "";
            }

            // 循环同步
            historyList.forEach((history) -> {

                // 判断是否已经同步过来
                String chargeNo = history.getInvestorID() + DateUtil.format(history.getApplyTime(), "yyMMddHHmm") + history.getAPID();
                MemberCharge memberCharge = chargeService.findByChargeNo(chargeNo);

                if (null == memberCharge) {

                    Member member = memberService.findByUnionId(history.getInvestorID());
                    if (null != member) {

                        ResResult<MemberCard> result = chargeService.findCardByMemberNo(member.getMemberNo());
                        if (result.isSuccess()) {
                            // 没有再添加
                            memberCharge = new MemberCharge();
                            memberCharge.setMemberNo(member.getMemberNo());
                            if (history.getApplyType() == 0) {
                                memberCharge.setChargeType(ChargeTypeEnum.CHARGE.getValue());
                            } else {
                                memberCharge.setChargeType(ChargeTypeEnum.WITHDRAW.getValue());
                            }
                            memberCharge.setChargeStatus(ChargeStatusEnum.STATUS_PROCESS.getValue());
                            memberCharge.setUpdatedAt(DateUtil.now());
                            memberCharge.setCreatedAt(history.getApplyTime());
                            memberCharge.setChargeNo(chargeNo);
                            memberCharge.setCardNum(result.getData().getCardNum());
                            memberCharge.setChargeQuotas(history.getApplyAmount());
                            memberCharge.setChargePoints(0);
                            memberCharge.setPayType("Balance");
                            memberCharge.setChargeMemo(history.getAuditRemark());

                            ResResult resResult = chargeService.add(memberCharge);
                            if (resResult.isSuccess()) {
                                // 更改为5、交易处理中
                                history.setAuditState(5);
                                updateApplyHistory(history);
                                LogUtil.info(String.format("订单%s同步成功", chargeNo));
                            }
                        } else {
                            LogUtil.info(String.format("用户%s未绑卡", history.getInvestorID()));
                        }
                    } else {
                        LogUtil.info(String.format("用户%s不存在", history.getInvestorID()));
                    }
                } else {
                    if (memberCharge.getChargeStatus().equals(ChargeStatusEnum.STATUS_SUCCESS.getValue())) {
                        // 更改为3、交易成功
                        history.setAuditState(3);
                        history.setAuditTime(DateUtil.now());
                        history.setAuditUser("系统");
                        history.setAuditRemark("");
                        updateApplyHistory(history);
                        LogUtil.info(String.format("订单%s交易成功", chargeNo));
                    } else if (memberCharge.getChargeStatus().equals(ChargeStatusEnum.STATUS_FAIL.getValue())) {
                        // 更改为4、交易失败
                        history.setAuditState(4);
                        history.setAuditTime(DateUtil.now());
                        history.setAuditUser("系统");
                        history.setAuditRemark("");
                        updateApplyHistory(history);
                        LogUtil.info(String.format("订单%s交易失败", chargeNo));
                    } else {
                        LogUtil.info(String.format("订单%s仍在处理中", chargeNo));
                    }
                }
            });

            return String.format("已处理%s条数据", historyList.size());
        } catch (Exception e) {
            LogUtil.error(e);
            return e.getMessage();
        }
    }

    //@GetMapping("/charge/process")
    public String chargeProcess() {

        try {

            // 查询需要处理的strive订单，处理中
            ChargeQueryDto queryDto = new ChargeQueryDto();
            queryDto.setChargeStatus(ChargeStatusEnum.STATUS_PROCESS.getValue());
            PageInfo<MemberCharge> memberChargePageInfo = chargeService.findMemberChargeList(queryDto);
            if (null == memberChargePageInfo || CollectionUtils.isEmpty(memberChargePageInfo.getList())) {
                LogUtil.warn(String.format("无需要处理的充值提现数据"));
                return "";
            }

            List<MemberCharge> memberChargeList = memberChargePageInfo.getList();
            if (null != memberChargeList) {
                memberChargeList.forEach((charge) -> {

                    if (charge.getChargeType().equals(ChargeTypeEnum.CHARGE.getValue())) {
                        ResResult resResult = chargeService.channelChargeQuery(charge);
                        if (null == resResult) {
                            // 若通道不存在订单，则开始支付
                            LogUtil.info(String.format("订单%s未支付，正在重新支付", charge.getChargeNo()));
                            chargeService.channelCharge(charge);
                        } else if (resResult.isSuccess()) {
                            LogUtil.info(String.format("订单%s支付成功", charge.getChargeNo()));
                            charge.setChargeStatus(ChargeStatusEnum.STATUS_SUCCESS.getValue());
                            charge.setUpdatedAt(DateUtil.now());
                            charge.setChargeMemo("通道返回码和返回消息");
                            chargeService.modify(ChargeStatusEnum.STATUS_PROCESS.getValue(), charge);
                        } else if (resResult.isSuccess()) {
                            LogUtil.info(String.format("订单%s支付失败", charge.getChargeNo()));
                            charge.setChargeStatus(ChargeStatusEnum.STATUS_FAIL.getValue());
                            charge.setUpdatedAt(DateUtil.now());
                            charge.setChargeMemo("通道返回码和返回消息");
                            chargeService.modify(ChargeStatusEnum.STATUS_PROCESS.getValue(), charge);
                        } else {
                            LogUtil.info(String.format("订单%s支付处理中", charge.getChargeNo()));
                        }
                    } else {
                        ResResult resResult = chargeService.channelWithdrawQuery(charge);
                        if (null == resResult) {
                            // 若通道不存在订单，则开始提现
                            LogUtil.info(String.format("订单%s未提现，正在重新提现", charge.getChargeNo()));
                            chargeService.channelWithdraw(charge);
                        } else if (resResult.isSuccess()) {
                            LogUtil.info(String.format("订单%s提现成功", charge.getChargeNo()));
                            charge.setChargeStatus(ChargeStatusEnum.STATUS_SUCCESS.getValue());
                            charge.setUpdatedAt(DateUtil.now());
                            charge.setChargeMemo("通道返回码和返回消息");
                            chargeService.modify(ChargeStatusEnum.STATUS_PROCESS.getValue(), charge);
                            //chargeService
                        } else if (resResult.isSuccess()) {
                            LogUtil.info(String.format("订单%s提现失败", charge.getChargeNo()));
                            charge.setChargeStatus(ChargeStatusEnum.STATUS_FAIL.getValue());
                            charge.setUpdatedAt(DateUtil.now());
                            charge.setChargeMemo("通道返回码和返回消息");
                            chargeService.modify(ChargeStatusEnum.STATUS_PROCESS.getValue(), charge);
                        } else {
                            LogUtil.info(String.format("订单%s提现处理中", charge.getChargeNo()));
                        }
                    }

                });
            }

            return String.format("已处理%s条数据", memberChargeList.size());
        } catch (Exception e) {
            LogUtil.error(e);
            return e.getMessage();
        }
    }

    @GetMapping("/interest/process")
    private void calInterest() {

        try {

            OrderQueryDto queryDto = new OrderQueryDto();
            queryDto.setOrderStatus(String.format("%s,%s", OrderStatusEnum.STATUS_PAID.getValue(), OrderStatusEnum.STATUS_DELIVERED.getValue()));

            List<MemberOrder> orderList = orderService.findOrderListForInterest(queryDto);

            if (null != orderList) {
                orderList.stream().forEach((order) -> {

                    Product product = productService.findByProductNo(order.getOrderProductNo());
                    if (null == product || StringUtil.isEmtpy(product.getProductUnit()) || product.getProductUnit().equals("0")) {
                        LogUtil.warn(String.format("产品%s不存在或配置不正确", order.getOrderProductNo()));
                        return;
                    }

                    if (null == order.getPayAt()) {
                        LogUtil.warn(String.format("订单%s支付日期不正确", order.getOrderNo()));
                        return;
                    }

                    if (StringUtil.isEmtpy(order.getDeliverMemo()) || order.getDeliverMemo().equals("0")) {
                        LogUtil.warn(String.format("订单%s计息费率不正确", order.getOrderNo()));
                        return;
                    }

                    // 产品周期
                    Integer rateType = Integer.valueOf(product.getProductUnit());
                    // 订单金额
                    BigDecimal orderQuotas = order.getOrderPrice().multiply(order.getOrderAmount());
                    // 年化利率
                    BigDecimal annualRate = new BigDecimal(order.getDeliverMemo());
                    // 计息时间
                    Date interestAt = null;

                    List<MemberOrderInterest> orderInterestList = orderService.findInterestByOrderNo(order.getOrderNo());
                    if (!CollectionUtils.isEmpty(orderInterestList)) {
                        Optional<Date> max = orderInterestList.stream().map(MemberOrderInterest::getInterestAt).max(Date::compareTo);
                        interestAt = max.get();
                    }

                    if (null == interestAt) {
                        // T+1计息
                        interestAt = DateUtil.date(DateUtil.addDays(order.getPayAt(), 1));
                    } else {
                        // 比如最后一次是23号，此时应计算24号
                        interestAt = DateUtil.date(DateUtil.addDays(interestAt, 1));
                    }

                    // 只有25号才能计算24号的数据
                    if (!DateUtil.now().after(DateUtil.addDays(interestAt, 1))) {
                        return;
                    }

                    /**
                     * 为了作直观比较，投资者可以将日万份收益折算成年化收益率。
                     * 比如某基金在昨天的日万份单位收益为0.25元，由于货币基金每天净值归一，
                     * 折合每单位收益率就是0.25元/10000/1元=0.000025，
                     * 年化收益率就是0.000025*365*100%=0.9125%。
                     *
                     *
                     *
                     *
                     *
                     * 两者之间有一个公式：七日年化收益率=（过去七天万份收益总和÷7）×365÷10000。
                     * 七日年化收益率是指货币基金近七日的平均收益率，
                     * 然后进行年化得来的数据。万份收益是指当天持有1万元货币基金所获得的收益。
                     *
                     * 年化=值/100
                     *
                     * 万份收益=(年化*10000)/365
                     *
                     * 1份收益=(年化)/365
                     */

                    // 计算规则，年化利率转单日计息利率

                    BigDecimal interestRate = (annualRate.divide(new BigDecimal(100)).multiply(new BigDecimal(10000))).divide(new BigDecimal(365), 4).divide(new BigDecimal(10000));

                    BigDecimal interestQuotas = orderQuotas.multiply(interestRate);

                    MemberOrderInterest inter = new MemberOrderInterest();
                    inter.setOrderNo(order.getOrderNo());
                    inter.setMemberNo(order.getMemberNo());
                    inter.setProductNo(order.getOrderProductNo());
                    inter.setOrderQuotas(orderQuotas);
                    // 记录的时候只记录万份收益
                    inter.setInterestRate(interestRate.multiply(new BigDecimal(10000)));
                    inter.setInterestQuotas(interestQuotas);
                    inter.setInterestAt(interestAt);
                    inter.setCreatedAt(DateUtil.now());
                    inter.setUpdatedAt(DateUtil.now());
                    inter.setInterestMemo("");

                    MemberOrderInterest interest = orderService.addInterest(inter);
                    AssertUtil.notNull(interest, String.format("订单利息计算失败：%s，%s", order.getOrderNo(), DateUtil.format(interestAt)));

                    if (orderInterestList.size() == (rateType.intValue() - 1)) {

                        orderInterestList.add(interest);
                        BigDecimal award = orderInterestList.stream().map(MemberOrderInterest::getInterestQuotas).reduce(BigDecimal.ZERO, BigDecimal::add);
                        order.setOrderAwardQuotas(award);
                        order.setOrderAwardResult("收益");
                        // 已经是最后一次计息，计息完成
                        boolean ret = orderService.updateStatus(order, order.getOrderStatus(), OrderStatusEnum.STATUS_REVOKED.getValue());
                        AssertUtil.isTrue(ret, String.format("订单利息计算失败：%s，%s", order.getOrderNo(), DateUtil.format(interestAt)));
                    } else {
                        // 不是最后一次计息
                        boolean ret = orderService.updateStatus(order, order.getOrderStatus(), OrderStatusEnum.STATUS_DELIVERED.getValue());
                        AssertUtil.isTrue(ret, String.format("订单利息计算失败：%s，%s", order.getOrderNo(), DateUtil.format(interestAt)));
                    }

                    LogUtil.info(String.format("订单利息计算成功：%s，%s", order.getOrderNo(), DateUtil.format(interestAt)));
                });
            }
        } catch (Exception e) {
            LogUtil.error(e);
        }
    }

    private List<_ApplyHistory> getApplyHistoryByStatus(String status) {
        try {

            Reader reader = Resources.getResourceAsReader("mybatisconfig_sql.xml");

            // 创建
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            // 解析资源
            SqlSessionFactory factory = builder.build(reader);

            // 打开session
            SqlSession session = factory.openSession();
            Statement statement = session.getConnection().createStatement();

            // 执行sql查询
            ResultSet resultSet = statement.executeQuery(" select * from ApplyHistory where AuditState IN (" + status + ") and AuditTime>DATEADD(day,-7,GETDATE()) and AuditRemark not like '%已处理%'");
            List<Map> list = MapUtil.toList(resultSet);

            List<_ApplyHistory> historyList = list.stream().collect(Collectors.mapping(s -> {

                String json = JsonUtil.toJson(s);
                _ApplyHistory history = JsonUtil.fromJson(json, _ApplyHistory.class);

                return history;
            }, Collectors.toList()));

            statement.close();
            session.close();

            return historyList;

        } catch (Exception e) {
            LogUtil.error(e);
            return null;
        }
    }

    private int updateInvestor(Member member) {
        try {

            Reader reader = Resources.getResourceAsReader("mybatisconfig_sql.xml");

            // 创建
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            // 解析资源
            SqlSessionFactory factory = builder.build(reader);

            // 打开session
            SqlSession session = factory.openSession();
            Statement statement = session.getConnection().createStatement();

            String sql = " update Investor" +
                    " set " +
                    //" Deposit=" + member.getMemberDeposit() + "," +
                    //" Withdraw=" + member.getMemberWithdraw() + "," +
                    " BuyAction=" + member.getMemberBuyAction() + "," +
                    " SellAction=" + member.getMemberSellAction() + "," +
                    " InterestAction=" + member.getMemberInterestAction() + "" +
                    " where InvestorID='" + member.getUnionId() + "'";

            int ret = statement.executeUpdate(sql);

            statement.close();
            session.close();

            if (ret > 0) {
                LogUtil.info("信息更新成功");
            }

            return ret;

        } catch (Exception e) {
            LogUtil.error(e);
            return -1;
        }
    }

    private int updateApplyHistory(_ApplyHistory history) {
        try {

            Reader reader = Resources.getResourceAsReader("mybatisconfig_sql.xml");

            // 创建
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            // 解析资源
            SqlSessionFactory factory = builder.build(reader);

            // 打开session
            SqlSession session = factory.openSession();
            Statement statement = session.getConnection().createStatement();

            String sql = " update ApplyHistory" +
                    " set " +
                    " AuditState='" + history.getAuditState() + "'," +
                    " AuditTime=getdate()," +
                    " AuditUser='" + history.getAuditUser() + "'," +
                    " AuditRemark='" + history.getAuditRemark() + "'" +
                    " where APID='" + history.getAPID() + "'";

            int ret = statement.executeUpdate(sql);

            statement.close();
            session.close();

            if (ret > 0) {
                LogUtil.info("信息更新成功");
            }

            return ret;

        } catch (Exception e) {
            LogUtil.error(e);
            return -1;
        }
    }
}

@Data
class _Investor {
    public String InvestorID;
    public BigDecimal Deposit;
    public BigDecimal Withdraw;
    public BigDecimal BuyAction;
    public BigDecimal SellAction;
    public BigDecimal InterestAction;
}

@Data
class _ApplyHistory {
    public int APID;
    public String InvestorID;
    public String InvestorName;
    public Date ApplyTime;
    public int ApplyType;
    public BigDecimal ApplyAmount;
    public String ApplyRemark;
    public Date AuditTime;
    public String AuditUser;
    // AuditState 0、待审核，1、审核通过，2、审核驳回，3、交易成功，4、交易失败，5、交易处理中
    public int AuditState;
    public String AuditRemark;
}
