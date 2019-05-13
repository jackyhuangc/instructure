package com.jacky.strive.api.controller;

import com.jacky.strive.common.*;
import com.jacky.strive.dao.model.Member;
import com.jacky.strive.dao.model.MemberCard;
import com.jacky.strive.dao.model.MemberCharge;
import com.jacky.strive.service.UserService;
import com.jacky.strive.service.enums.ChargeStatusEnum;
import com.jacky.strive.service.enums.ChargeTypeEnum;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jacky.strive.common.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/5 21:27
 * @since jdk1.8
 */
//@RestController
@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String testConnect(Principal user) {
        Logger logger = LoggerFactory.getLogger(HomeController.class);
        logger.warn("sentry错误测试。。。");
        return userService.testConnnect();
    }

    /**
     * 本地访问内容地址 ：http://localhost:8080/hello
     *
     * @param map
     * @return
     */
    @RequestMapping("/hello")
    public String helloHtml(HashMap<String, Object> map) {
        map.put("hello", "欢迎进入HTML页面");
        return "/index";
    }

    /**
     * 本地访问内容地址 ：http://localhost:8080/
     *
     * @param model
     * @return
     */
    @RequestMapping("/pay_back")
    public String payBack(_CodePay codePay, Model model) {

        model.addAttribute("TradeNO", codePay.getTno());
        model.addAttribute("OrderNO", codePay.getPayno());
        model.addAttribute("Money", codePay.getMoney());
        model.addAttribute("PayType", codePay.getTyp());

        //$appid."|".$appkey."|".$tno."|".$payno."|".$money."|".$paytime."|".$typ
        //2018070632|facfc63c930ba0a1694da564dce27e00|20181204200040011100600025832744|123456|0.01|2018-12-04 15:26:00|2
        //验签
//        String signStr = String.format("%s|%s|%s|%s|%s|%s|%s", codePay.getAppid(), "facfc63c930ba0a1694da564dce27e00", codePay.getTno(), codePay.getPayno(), codePay.getMoney(), codePay.getPaytime(), codePay.getTyp());
//        if (!Md5Util.encode(signStr).equalsIgnoreCase(codePay.getSign())) {
//            model.addAttribute("ErrorCode", "9999");
//            model.addAttribute("ErrorMessage", "验签失败，请联系管理员");
//            return "pay_back";
//        }

        String investorID = codePay.getPayno().substring(0, 4);
        String apid = codePay.getPayno().substring(14);

        _ApplyHistory history = getApplyHistoryByID(investorID, apid);
        if (null != history) {
            // 判断是否已经同步过来
            //String chargeNo = history.getInvestorID() + DateUtil.format(history.getApplyTime(), "yyMMddHHmm") + history.getAPID();
            //2018-12-04 07:11:33.930   1020181204071131

            // 补充备注信息
            if (!StringUtil.isEmpty(codePay.getRemark())) {
                codePay.setRemark(String.format("交易订单号:%s-%s", codePay.getPayno(), codePay.getRemark()));
            } else {
                codePay.setRemark(String.format("交易订单号:%s", codePay.getPayno()));
            }

            if (history.getAuditState() == 3) {
//                model.addAttribute("ErrorCode", "9999");
//                model.addAttribute("ErrorMessage", "订单已处理，重复通知");
                model.addAttribute("ErrorCode", "0000");
                model.addAttribute("ErrorMessage", "");

//                String content = "您的充值订单[order_no]于[date_time]交易成功,交易金额[money]元";
//                content = content.replace("[order_no]", codePay.getPayno());
//                content = content.replace("[date_time]", codePay.getPaytime());
//                content = content.replace("[money]", codePay.getMoney());
//
//                // 如果是第一次更新，则发送短信
//                SmsUtil.sendSms(history.getTelephone(), content);
            } else {

                // 注意小数金额容错机制，实际支付金额可能大于订单金额
                if (history.getApplyAmount().compareTo(new BigDecimal(codePay.getMoney())) <= 0) {

                    // 更改为3、交易成功
                    history.setAuditState(3);
                    history.setAuditTime(DateUtil.now());
                    history.setAuditUser("系统");
                    history.setAuditRemark(codePay.getRemark());

                    int ret = updateApplyHistory(history);
                    if (ret > -1) {
                        model.addAttribute("ErrorCode", "0000");
                        model.addAttribute("ErrorMessage", "");
                        if (ret > 0) {

                            String content = "您的充值订单[order_no]于[date_time]交易成功,交易金额[money]元";
                            content = content.replace("[order_no]", codePay.getPayno());
                            content = content.replace("[date_time]", codePay.getPaytime());
                            content = content.replace("[money]", codePay.getMoney());

                            // 如果是第一次更新，则发送短信，HQSC要求不发充值短信
                            //SmsUtil.sendSms(history.getTelephone(), content);
                        }
                    } else {
                        model.addAttribute("ErrorCode", "9999");
                        model.addAttribute("ErrorMessage", "交易失败，请联系管理员");
                    }
                } else {
                    model.addAttribute("ErrorCode", "9999");
                    model.addAttribute("ErrorMessage", "交易失败，订单金额不匹配");
                }
            }
        } else {
            model.addAttribute("ErrorCode", "9999");
            model.addAttribute("ErrorMessage", "交易不存在");
        }

        return "pay_back";
    }

    private _ApplyHistory getApplyHistoryByID(String investorID, String apid) {
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
            ResultSet resultSet = statement.executeQuery(String.format(" select ApplyHistory.*,Investor.Telephone from ApplyHistory inner join Investor on ApplyHistory.InvestorID=Investor.InvestorID where ApplyHistory.InvestorID='%s' and APID=%s", investorID, apid));
            List<Map> list = MapUtil.convertList(resultSet);

            List<_ApplyHistory> historyList = list.stream().collect(Collectors.mapping(s -> {

                String json = JsonUtil.toJson(s);
                _ApplyHistory history = JsonUtil.fromJson(json, _ApplyHistory.class);

                return history;
            }, Collectors.toList()));

            statement.close();
            session.close();

            if (CollectionUtils.isEmpty(historyList)) {
                return null;
            }

            return historyList.get(0);

        } catch (Exception e) {
            LogUtil.error(e);
            return null;
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
                    " where APID='" + history.getAPID() + "' and AuditState<>'" + history.getAuditState() + "'";

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
    public String Telephone;
}

@Data
class _CodePay {
    // http://pay2.58eq.cn/pay/pay.php?appid=2018070632&tno=1020654321&typ=2&payno=1020&money=0.01
    // 返回信息如下
    // http://180.76.234.157:6677/?appid=2018070632&tno=20181204200040011100600026044646&payno=1020&money=0.01&paytime=2018-12-04+17%3A53%3A45&typ=2&sign=b2a31240dcb9259fae7255669a20a430
    private String appid;
    // 平台交易号
    private String tno;
    // 业务订单号(用户备注)
    private String payno;
    private String money;
    private String paytime;
    private String typ;
    private String sign;
    private String remark;
}