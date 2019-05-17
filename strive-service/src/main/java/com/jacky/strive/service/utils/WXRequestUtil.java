package com.jacky.strive.service.utils;

import com.jacky.common.util.*;
import com.jacky.common.*;
import com.jacky.common.entity.result.ResResult;
import com.jacky.strive.service.dto.WeChat.WxPrepay;
import com.jacky.strive.service.dto.WeChat.WxSession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.security.KeyStore;
import java.util.*;

/**
 * WXRequestUtil
 */
public class WXRequestUtil {

    /**
     * 统一下单
     */
    public static String UNIFIED_ORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 支付查询API
     */
    public static String ORDER_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 退款API
     */
    public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 退款查询API
     */
    public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";

    /**
     * 转换短链接
     */
    public static String SHORT_URL_API = "https://api.mch.weixin.qq.com/tools/shorturl";

    /**
     * 关闭订单
     */
    public static String CLOSE_ORDER_API = "https://api.mch.weixin.qq.com/pay/closeorder";

    public static String APP_ID = "wx2421b1c4370ec43b";
    public static String API_KEY = "wx2421b1c4370ec43b";
    public static String MCH_ID = "10000100";
    public static String SECRET = "xxxxx";

    public static void main(String[] args) {

        Integer str = "WX_JSAPI".indexOf("X");
        ResResult resResult = PrePayment("20170106113324", BigDecimal.valueOf(1.00), "JSAPI", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o", "苹果", "");

        resResult = WxLogin("xxx");
        resResult = PaymentQuery("20170106113324");
        assert null != resResult;
    }

    public static ResResult<WxSession> WxLogin(String jsCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APP_ID + "&secret=" + SECRET + "&js_code=" + jsCode + "&grant_type=authorization_code";
        String res = HttpUtil.get(url, null);
        if (StringUtil.isEmtpy(res)) {
            return ResResult.fail("操作失败");
        }

        WxSession wxSession = JsonUtil.fromJson(res, WxSession.class);
        if (StringUtil.isEmtpy(wxSession.getOpenId())) {
            return ResResult.fail(wxSession.getErrMsg(), wxSession);
        }

        return ResResult.success(wxSession);
    }

    public static ResResult<WxPrepay> PrePayment(String out_trade_no, BigDecimal total_fee, String trade_type, String openid, String desc) {

        return PrePayment(out_trade_no, total_fee, trade_type, openid, desc, "");
    }

    public static ResResult<WxPrepay> PrePayment(String out_trade_no, BigDecimal total_fee, String trade_type, String openid, String body, String product_id) {

        String xml = GenerateParamForPrePay(out_trade_no, total_fee, trade_type, openid, body, product_id);
        String res = HttpUtil.httpRequest(UNIFIED_ORDER_API, "POST", xml);

        Map<String, String> map = null;
        try {
            map = doXMLParse(res);
        } catch (Exception e) {
        }

        String code = map.get("return_code");
        String resultCode = map.get("result_code");

        if (code.equals("SUCCESS")) {
            if (resultCode.equals("SUCCESS")) {

                // 交易成功
                WxPrepay resPrepay = new WxPrepay();
                resPrepay.setTradeType(map.get("trade_type"));
                resPrepay.setPrepayId(map.get("prepay_id"));
                resPrepay.setCodeUrl(map.get("code_url"));
                resPrepay.setAppId(map.get("appid"));
                resPrepay.setMchId(map.get("mch_id"));
                resPrepay.setNonceStr(map.get("nonce_str"));
                resPrepay.setTimeStamp(String.valueOf(DateUtil.getTimestamp()));

                String param = String.format("appId=%s&nonceStr=%s&package=prepay_id=%s&signType=MD5&timeStamp=%s&key=%s",
                        resPrepay.getAppId(), resPrepay.getNonceStr(), resPrepay.getPrepayId(), resPrepay.getTimeStamp(), API_KEY);

                resPrepay.setPaySign(Md5Util.encode(param).toUpperCase());

                return ResResult.process("等待支付", resPrepay);
            } else {
                // 交易失败
                return ResResult.fail("交易失败");
            }
        } else {
            // 交易失败
            return ResResult.fail(map.get("return_msg"));
        }
    }

    public static ResResult<String> PaymentQuery(String out_trade_no) {

        String xml = GenerateParamForQuery(out_trade_no);

        String res = HttpUtil.httpRequest(ORDER_QUERY_API, "POST", xml);

        Map<String, String> map = null;
        try {
            map = doXMLParse(res);
            String str = JsonUtil.toJson(map);
        } catch (Exception e) {

            return ResResult.process("处理中", "");
        }

        String code = map.get("return_code");
        String tradeCode = map.get("trade_state");

        if (code.equals("SUCCESS")) {
            if (tradeCode.equals("SUCCESS")) {

                // SUCCESS—支付成功
                return ResResult.success("支付成功", "");

            } else if (tradeCode.equals("CLOSED") || tradeCode.equals("REVOKED") || tradeCode.equals("PAYERROR")) {

                // CLOSED—已关闭 REVOKED—已撤销（刷卡支付）PAYERROR--支付失败(其他原因，如银行返回失败)
                return ResResult.fail("支付失败", "");
            } else {

                // NOTPAY—未支付 REFUND—转入退款 USERPAYING--用户支付中 支付状态机请见下单API页面
                return ResResult.process("处理中", "");
            }
        }

        return ResResult.process("处理中", map.get("return_msg"));
    }

    public static ResResult<String> Refund(String out_trade_no, String out_refund_no, BigDecimal total_fee, BigDecimal refund_fee) {

        SortedMap<String, String> param = new TreeMap<>();

        //应用id
        param.put("appid", APP_ID);
        //商户号
        param.put("mch_id", MCH_ID);
        //随机字符串
        param.put("nonce_str", StringUtil.getUniqueString(""));
        //订单号
        param.put("out_trade_no", out_trade_no);
        //退款单号
        param.put("out_refund_no", out_refund_no);
        //订单总金额Utils.getMoney()
        param.put("total_fee", String.valueOf(total_fee));
        //退款总金额
        param.put("refund_fee", String.valueOf(refund_fee));

        String sign = GetSign(param);
        param.put("sign", sign);

        String xml = GetMapToXML(param);

        try {
            String response = doRefund(REFUND_API, xml);
            System.out.print(response);
            if (StringUtil.isEmtpy(response)) {
                return ResResult.fail("退款失败");
            }

            Map<String, String> map = doXMLParse(response);


            String returnCode = map.get("return_code");
            String resultCode = map.get("result_code");

            // 请求发送成功
            if (returnCode.equals("SUCCESS")) {
                if (resultCode.equals("SUCCESS")) {
                    return ResResult.success("退款接收成功");
                } else {
                    return ResResult.fail("退款接收失败");
                }
            }

            return ResResult.fail(map.get("return_msg"));
        } catch (Exception e) {

            return ResResult.process(e.getMessage(), null);
        }
    }

    public static ResResult<String> RefundQuery(String out_trade_no, String out_refund_no) {

        SortedMap<String, String> param = new TreeMap<>();

        //应用id
        param.put("appid", APP_ID);
        //商户号
        param.put("mch_id", MCH_ID);
        //随机字符串
        param.put("nonce_str", StringUtil.getUniqueString(""));
        //订单号
        param.put("out_trade_no", out_trade_no);
        //退款单号
        param.put("out_refund_no", out_refund_no);

        String sign = GetSign(param);
        param.put("sign", sign);

        String xml = GetMapToXML(param);
        LogUtil.info("*********************请求参数*************************");
        LogUtil.info(xml);
        LogUtil.info("*****************************************************");

        try {
            String response = HttpUtil.httpRequest(REFUND_QUERY_API, "POST", xml);
            LogUtil.info("*********************响应结果*************************");
            LogUtil.info(response);
            LogUtil.info("*****************************************************");
            if (StringUtil.isEmtpy(response)) {
                return ResResult.fail("退款查询失败");
            }

            Map<String, String> map = doXMLParse(response);

            String returnCode = map.get("return_code");
            String resultCode = map.get("result_code");
            String refundCode = map.get("refund_status_0");

            if (returnCode.equals("SUCCESS")) {
                if (resultCode.equals("SUCCESS")) {
                    if (refundCode.equals("SUCCESS")) {
                        return ResResult.success("退款成功");
                    } else if (refundCode.equals("REFUNDCLOSE")) {
                        return ResResult.fail("退款失败");
                    } else {
                        return ResResult.process("退款处理中/退款异常", null);
                    }
                } else {
                    return ResResult.fail("退款接收失败");
                }
            }

            return ResResult.fail(map.get("return_msg"));
        } catch (Exception e) {

            return ResResult.process(e.getMessage(), null);
        }
    }

    public static ResResult<String> CloseOrder(String out_trade_no) {

        SortedMap<String, String> param = new TreeMap<>();

        //应用id
        param.put("appid", APP_ID);
        //商户号
        param.put("mch_id", MCH_ID);
        //随机字符串
        param.put("nonce_str", StringUtil.getUniqueString(""));
        //订单号
        param.put("out_trade_no", out_trade_no);

        String sign = GetSign(param);
        param.put("sign", sign);

        String xml = GetMapToXML(param);

        LogUtil.info("*********************请求参数*************************");
        LogUtil.info(xml);
        LogUtil.info("*****************************************************");

        try {
            String response = HttpUtil.httpRequest(CLOSE_ORDER_API, "POST", xml);

            LogUtil.info("*********************响应结果*************************");
            LogUtil.info(response);
            LogUtil.info("*****************************************************");
            if (StringUtil.isEmtpy(response)) {
                return ResResult.fail("退款查询失败");
            }

            Map<String, String> map = doXMLParse(response);

            String returnCode = map.get("return_code");
            String resultCode = map.get("result_code");

            if (returnCode.equals("SUCCESS")) {
                if (resultCode.equals("SUCCESS")) {
                    return ResResult.success("关单成功");
                } else {
                    return ResResult.fail(map.get("result_msg"));
                }
            }

            return ResResult.fail(map.get("return_msg"));
        } catch (Exception e) {

            return ResResult.process(e.getMessage(), null);
        }
    }

    private static String GetIp() {
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            String localip = ia.getHostAddress();
            return localip;
        } catch (Exception e) {
            return null;
        }
    }

    private static String GetSign(Map<String, String> param) {
        String StringA = formatUrlMap(param, false, false);
        String stringSignTemp = Md5Util.encode((StringA + "&key=" + API_KEY).toUpperCase());
        return stringSignTemp;
    }

    private static String GetMapToXML(Map<String, String> param) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append("<" + entry.getKey() + ">");
            sb.append(entry.getValue());
            sb.append("</" + entry.getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    private static String GenerateParamForPrePay(String out_trade_no, BigDecimal
            total_fee, String trade_type, String openid, String description, String product_id) {

        //订单总金额，单位为分，详见支付金额
        int fee = (int) (total_fee.doubleValue() * 100.00);
        Map<String, String> param = new HashMap<String, String>();
        param.put("appid", APP_ID);
        param.put("mch_id", MCH_ID);
        param.put("nonce_str", StringUtil.getUniqueString(""));
        param.put("body", description);
        param.put("out_trade_no", out_trade_no);
        param.put("total_fee", fee + "");
        param.put("spbill_create_ip", GetIp());
        param.put("notify_url", "");
        //交易类型	trade_type 小程序取值如下：JSAPI JSAPI--公众号支付/小程序、NATIVE--原生扫码支付、APP--app支付，MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
        param.put("trade_type", trade_type);
        //param.put("product_id", product_id + "");
        //指定支付方式	limit_pay	否	String(32)	no_credit	上传此参数no_credit--可限制用户不能使用信用卡支付
        //param.put("limit_pay",  "no_credit");
        //商品ID	product_id	否	String(32)	12235413214070356458058	trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
        //param.put("product_id", product_id + "");
        //用户标识	openid	否	String(128)	oUpF8uMuAJO_M2pxb1Q9zNjWeS6o	trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
        param.put("openid", openid);// "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");

        String sign = GetSign(param);

        param.put("sign", sign);
        return GetMapToXML(param);
    }

    private static String GenerateParamForQuery(String out_trade_no) {

        Map<String, String> param = new HashMap<String, String>();
        param.put("appid", APP_ID);
        param.put("mch_id", MCH_ID);
        param.put("out_trade_no", out_trade_no);
        param.put("nonce_str", StringUtil.getUniqueString(""));

        String sign = GetSign(param);

        param.put("sign", sign);
        return GetMapToXML(param);
    }

    private static Map<String, String> doXMLParse(String strxml) throws Exception {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map<String, String> m = new HashMap<String, String>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();
        return m;
    }

    private static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    /**
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写
     *                   true:key转化成小写，false:不转化
     * @return
     */
    private static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (!StringUtil.isEmtpy(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower) {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

    private static String doRefund(String url, String data) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream is = new FileInputStream(new File("D:/apiclient_cert.p12"));
        try {
            // 这里写密码..默认是你的MCHID
            keyStore.load(is, MCH_ID.toCharArray());
        } finally {
            is.close();
        }

        // 这里也是写密码的
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, MCH_ID.toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf).build();
        try {
            // 设置响应头信息
            HttpPost httpost = new HttpPost(url);
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                String jsonStr = EntityUtils.toString(response.getEntity(),
                        "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
