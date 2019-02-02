package com.jacky.strive.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import qsq.biz.common.constant.ContentType;
import qsq.biz.common.util.JsonUtil;
import qsq.biz.common.util.StringUtil;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * HttpUtil
 */
public class HttpUtil {

    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_FORM = "application/x-www-form-urlencoded";
    public static final String TEXT_HTML = "text/html";

    public static String get(String url, Map<String, String> headers) {
        //httpClient
        HttpClient httpClient = HttpClients.createDefault();

        // get method
        HttpGet httpGet = new HttpGet(url);

        LogUtil.info("*********************请求参数*************************");
        LogUtil.info(url);
        LogUtil.info("*****************************************************");
        // set header
        if (null != headers) {
            headers.entrySet().forEach(header -> {
                httpGet.addHeader(header.getKey(), header.getValue());
            });
        }

        //response
        HttpResponse response = null;
        String res = "";
        try {
            response = httpClient.execute(httpGet);

            //get response into String
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, "UTF-8");
            LogUtil.info("*********************响应结果*************************");
            LogUtil.info(res);
            LogUtil.info("*****************************************************");
        } catch (Exception e) {

        }

        return res;
    }

    public static String post(String uri, Object reqDto) {
        return post(uri, reqDto, APPLICATION_JSON, "UTF-8");
    }

    public static String post(String uri, Object reqDto, String contentType) {
        return post(uri, reqDto, contentType, "UTF-8");
    }

    public static String post(String uri, Object reqDto, String contentType, String charset) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String respStr = "";
        try {
            HttpPost postRequest = new HttpPost(uri);
            AbstractHttpEntity httpEntity = null;

            String reqStr = "";
            if (StringUtil.equals(ContentType.APPLICATION_FORM, contentType)) {
                reqStr = JsonUtil.toForm(reqDto);
            } else {
                reqStr = JsonUtil.toJson(reqDto);
            }

            if (!StringUtil.isEmtpy(reqStr)) {
                httpEntity = new StringEntity(reqStr, charset);
                httpEntity.setContentType(contentType);

                postRequest.setEntity(httpEntity);
                LogUtil.info("*********************请求参数*************************");
                LogUtil.info(reqStr);
                LogUtil.info("*****************************************************");
            }

            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(postRequest);
            try {
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity) {
                    respStr = EntityUtils.toString(entity);
                    LogUtil.info("*********************响应结果*************************");
                    LogUtil.info(respStr);
                    LogUtil.info("*****************************************************");
                }
            } finally {
                httpResponse.close();
            }

            return respStr;
        } catch (IOException e) {
            LogUtil.error(e);
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }

        return respStr;
    }

    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {

                LogUtil.info("*********************请求参数*************************");
                LogUtil.info(outputStr);
                LogUtil.info("*****************************************************");

                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
            String respStr = buffer.toString();

            LogUtil.info("*********************响应结果*************************");
            LogUtil.info(respStr);
            LogUtil.info("*****************************************************");

            return respStr;
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}" + ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}" + e);
        }
        return null;
    }
}
