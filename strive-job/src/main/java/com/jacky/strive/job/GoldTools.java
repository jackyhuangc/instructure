package com.jacky.strive.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jacky.strive.common.LogUtil;
import com.jacky.strive.common.MapUtil;
import lombok.Data;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import qsq.biz.common.util.JsonUtil;
import qsq.biz.common.util.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class GoldTools {

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY = "c74544220703b54eff49d00c2ebd8b02";

    //1.上海黄金交易所
    public static void runGold() {
        String result = null;
        String url = "http://web.juhe.cn:8080/finance/gold/shgold";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key", APPKEY);//APP Key
        params.put("v", "");//JSON格式版本(0或1)默认为0

        try {
            result = net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if (object.getInteger("error_code") == 0) {

                if (object.get("result") instanceof JSONArray) {

                    JSONArray jsonArray = (JSONArray) object.get("result");
                    if (jsonArray.size() > 0) {

                        List<_Product> productList = queryGold();
                        Map listGold = (JSONObject) jsonArray.get(0);
                        listGold.forEach((k, v) -> {

                            _Gold gold = JsonUtil.fromJson(JsonUtil.toJson(v), _Gold.class);

                            // 仅测试用
                            if (gold.getVariety().equals("Au100g")) {
                                gold.setVariety("llg");
                            }
                            updateGold(productList, gold);
                            LogUtil.info(JsonUtil.toJson(gold));
                        });
                    }
                }

            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2.上海期货交易所
    public static void runFuture() {
        String result = null;
        String url = "http://web.juhe.cn:8080/finance/gold/shfuture";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key", APPKEY);//APP Key
        params.put("v", "");//JSON格式版本(0或1)默认为0

        try {
            result = net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if (object.getInteger("error_code") == 0) {
                System.out.println(object.get("result"));
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //3.银行账户黄金
    public static void runBankGold() {
        String result = null;
        String url = "http://web.juhe.cn:8080/finance/gold/bankgold";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key", APPKEY);//APP Key

        try {
            result = net(url, params, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if (object.getInteger("error_code") == 0) {
                System.out.println(object.get("result"));
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * String(yyyy-MM-dd HH:mm:ss)转10位时间戳
     *
     * @param time
     * @return
     */
    public static Integer StringToTimestamp(String time) {

        int times = 0;
        try {
            times = (int) ((Timestamp.valueOf(time).getTime()) / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (times == 0) {
            System.out.println("String转10位时间戳失败");
        }
        return times;
    }

    public static List<_Product> queryGold() throws SQLException {
        try {
            Reader reader = Resources.getResourceAsReader("mybatisconfig.xml");

            // 创建
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

            // 解析资源
            SqlSessionFactory factory = builder.build(reader);

            // 打开session
            SqlSession session = factory.openSession();
            Statement statement = session.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(" select pi.pid,pi.ptitle,pi.procode from wp_productinfo as pi" +
                    " where pi.isdelete=0");

            List<Map> list = MapUtil.convertList(resultSet);

            List<_Product> productList = list.stream().collect(Collectors.mapping(s -> {
                _Product product = new _Product();
                Iterator<Map.Entry<String, String>> it = s.entrySet().iterator();
                while (it.hasNext()) {

                    Map.Entry map = it.next();
                    if (map.getKey().equals("pid")) {
                        product.setPid(String.valueOf(map.getValue()));
                    } else if (map.getKey().equals("ptitle")) {
                        product.setPtitle(String.valueOf(map.getValue()));
                    } else if (map.getKey().equals("procode")) {
                        product.setProcode(String.valueOf(map.getValue()));
                    }
                }

                return product;
            }, Collectors.toList()));

            return productList;
        } catch (Exception e) {
            LogUtil.error(e);
            return null;
        }
    }

    public static void updateGold(List<_Product> productList, _Gold gold) {

        if (!StringUtil.isEmtpy(gold.time)) {
            gold.setTime(String.valueOf(StringToTimestamp(gold.time)));
        }

        productList.stream().forEach((p) -> {

            if (p.getProcode().equals(gold.getVariety())) {

                try {
                    Reader reader = Resources.getResourceAsReader("mybatisconfig.xml");

                    // 创建
                    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();

                    // 解析资源
                    SqlSessionFactory factory = builder.build(reader);

                    // 打开session
                    SqlSession session = factory.openSession();
                    Statement statement = session.getConnection().createStatement();
                    int ret = statement.executeUpdate(" update wp_productdata\n" +
                            " set Open='" + gold.openpri + "',Close='" + gold.yespri + "'," +
                            " Price='" + gold.latestpri + "',Low='" + gold.minpri + "'," +
                            " High='" + gold.maxpri + "',UpdateTime='" + gold.time + "'\n" +
                            " where pid='" + p.getPid() + "'");

                    if (ret > 0) {
                        LogUtil.info("信息更新成功");
                    }

                } catch (Exception e) {
                    LogUtil.error(e);
                }

                return;
            }
        });
    }
}

@Data
class _Product {
    String pid;
    String ptitle;
    String procode;
}

@Data
class _Gold {

    /*品种*/
    @JsonProperty("variety")
    String variety;

    /*开盘价*/
    @JsonProperty("openpri")
    String openpri;

    /*昨收价*/
    @JsonProperty("yespri")
    String yespri;

    /*最新价*/
    @JsonProperty("latestpri")
    String latestpri;

    /*最高价*/
    @JsonProperty("maxpri")
    String maxpri;

    /*最低价*/
    @JsonProperty("minpri")
    String minpri;

    /*涨跌幅*/
    @JsonProperty("limit")
    String limit;

    /*总成交量*/
    @JsonProperty("totalvol")
    String totalvol;

    /*更新时间*/
    @JsonProperty("time")
    String time;
}
