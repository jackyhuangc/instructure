package com.jacky.strive.api.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jacky.common.util.JsonUtil;
import com.jacky.common.util.LogUtil;
import com.jacky.common.util.SmsUtil;
import com.jacky.common.util.StringUtil;
import com.jacky.common.entity.result.ResResult;
import com.jacky.strive.service.dto.PostEntityDto;
import com.jacky.strive.service.dto.SmsDto;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jacky.common.*;

@CrossOrigin() // 需支持跨域,否则客户端虽能正常访问，但也会报跨域异常
@RestController
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    private final ResourceLoader resourceLoader;

    @Autowired
    public CommonController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 文件上传具体实现方法（单文件上传）
     *
     * @param file
     * @return
     * @author 单红宇(CSDN CATOOP)
     * @create 2017年3月11日
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<String> upLoadToDefault(@RequestParam("file") MultipartFile file) {

        // 相对路径 文件夹路径
        return upLoad(file, "");
    }

    /**
     * 文件上传具体实现方法（单文件上传）
     *
     * @param file
     * @return
     * @author 单红宇(CSDN CATOOP)
     * @create 2017年3月11日
     */
    @RequestMapping(value = "/upload/{dir}", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<String> uploadToDirectory(@RequestParam("file") MultipartFile file, @PathVariable("dir") String dir) {

        return upLoad(file, dir);
    }

    /**
     * 多文件上传 主要是使用了MultipartHttpServletRequest和MultipartFile
     *
     * @param request
     * @return
     * @author 单红宇(CSDN CATOOP)
     * @create 2017年3月11日
     */
    @RequestMapping(value = "/upload/batch", method = RequestMethod.POST)
    public @ResponseBody
    ResResult<List<String>> batchUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;

        // 相对路径 文件夹路径
        String fileDir = "upload/";
        File myPath = new File(fileDir);
        // 若此目录不存在，则创建之
        if (!myPath.exists()) {
            myPath.mkdir();
            logger.info("创建文件夹路径为：" + fileDir);
        }

        List<String> listFilePath = new ArrayList<>();
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    String filePath = fileDir + file.getOriginalFilename();
                    listFilePath.add(filePath);

                    stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception ex) {
                    LogUtil.error(ex);
                    return ResResult.fail(i + "*" + ex.getMessage());
                }
            } else {
                return ResResult.fail("You failed to upload " + i + " because the file was empty.");
            }
        }

        return ResResult.success("", listFilePath);
    }

    /**
     * 显示图片的方法关键 匹配路径像 localhost:8080/b7c76eb3-5a67-4d41-ae5c-1642af3f8746.png
     *
     * @param filename
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/upload/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get("upload", filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/upload/{dir}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFileFromDirectory(@PathVariable String dir, @PathVariable String filename) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get("upload/" + dir, filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/sendSms/{mobile}/{content}")
    public ResResult<Boolean> sendSms(@PathVariable String mobile, @PathVariable String content) {

        if (SmsUtil.sendSms(mobile, content)) {
            return ResResult.success("发送成功");
        } else {
            return ResResult.fail("发送失败");
        }
    }

    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    @ResponseBody
    public ResResult<String> sendSms(@RequestBody SmsDto smsDto) {

        String sources = "0123456789";
        Random rand = new Random();
        StringBuffer randNum = new StringBuffer();
        for (int j = 0; j < 6; j++) {
            randNum.append(sources.charAt(rand.nextInt(9)) + "");
        }

        smsDto.setContent(smsDto.getContent()+randNum);

        if (SmsUtil.sendSms(smsDto.getMobile(), smsDto.getContent())) {
            return ResResult.success(randNum.toString());
        } else {
            return ResResult.fail("发送失败");
        }
    }

    @RequestMapping(value = "/proxy/post", method = RequestMethod.POST)
    @ResponseBody
    public String ProxyPost( @RequestBody PostEntityDto postEntity) {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> httpEntity;

        if (postEntity.getHeader() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", postEntity.getHeader());

            LinkedMultiValueMap map=convertJsonToMultiValueMap(JsonUtil.toJson(postEntity.getData()));
            httpEntity = new HttpEntity<Object>(map, headers);
        } else {

            LinkedMultiValueMap map=convertJsonToMultiValueMap(JsonUtil.toJson(postEntity.getData()));
            httpEntity = new HttpEntity<Object>(map);
        }

        String restResult = restTemplate.postForObject(postEntity.getUrl(), httpEntity, String.class);
        return restResult;
    }

    private LinkedMultiValueMap convertJsonToMultiValueMap(String jsonString)
    {
        JSONObject object = JSONObject.parseObject(jsonString);
        Iterator it = object.keySet().iterator();
        StringBuilder sb = new StringBuilder("{");
        while (it.hasNext()) {
            String key = (String) it.next();
            Object value = object.getString(key);
            if (value == null) {
                sb.append(JSON.toJSONString(key)).append(":").append(value).append(",");
            } else {
                List<String> list = Collections.singletonList(value.toString());
                sb.append(JSON.toJSONString(key)).append(":").append(JSON.toJSONString(list)).append(",");
            }
        }
        sb.append("}");
        sb.deleteCharAt(sb.length()-2);
        System.out.println(sb.toString());
        LinkedMultiValueMap params = JSON.parseObject(sb.toString(), LinkedMultiValueMap.class);

        return params;
    }


    private ResResult<String> upLoad(MultipartFile file, String toDir) {

        if (!file.isEmpty()) {

            String fileDir = "upload/";
            try {

                if (!StringUtil.isEmtpy(toDir)) {
                    fileDir = fileDir + toDir + "/";
                }

                File myPath = new File(fileDir);
                // 若此目录不存在，则创建之
                if (!myPath.exists()) {
                    myPath.mkdir();
                    logger.info("创建文件夹路径为：" + fileDir);
                }

                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(fileDir + file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (Exception ex) {
                LogUtil.error(ex);
                return ResResult.fail(ex.getMessage());
            }

            return ResResult.success("上传成功", "/" + fileDir + file.getOriginalFilename());
        } else {

            return ResResult.fail("empty file");
        }
    }

    /**
     * 文件上传具体实现方法（单文件上传）
     *
     * @param file
     * @return
     * @author 单红宇(CSDN CATOOP)
     * @create 2017年3月11日
     */
    @RequestMapping(value = "/upload2layui/{dir}", method = RequestMethod.POST)
    @ResponseBody
    public LayuiResult upLoadToLayui(@RequestParam("file") MultipartFile file, @PathVariable("dir") String dir) {
        LayuiResult result = new LayuiResult();
        result.setCode(1);
        result.setMsg("失败");
        if (!file.isEmpty()) {

            String fileDir = "upload/";
            String fileOSDir = "static/Content/images/";
            try {

                if (!StringUtil.isEmtpy(dir)) {
                    fileDir = fileDir + dir + "/";
                    fileOSDir = fileOSDir + dir + "/";
                }

                File myPath = new File(fileDir);
                // 若此目录不存在，则创建之
                if (!myPath.exists()) {
                    myPath.mkdir();
                    logger.info("创建文件夹路径为：" + fileDir);
                }

                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(fileDir + file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();


                FileOutputStream os = new FileOutputStream(new File("strive-web/" + fileOSDir + file.getOriginalFilename()));
                FileCopyUtils.copy(file.getBytes(), os);

            } catch (Exception ex) {
                LogUtil.error(ex);
                result.setMsg(ex.getMessage());
            }

            LayuiData data = new LayuiData();
            data.setSrc("/" + fileOSDir + file.getOriginalFilename());
            data.setTitle(file.getOriginalFilename());
            result.setCode(0);
            result.setMsg("上传成功");
            result.setData(data);
        } else {
            result.setMsg("empty file");
        }

        return result;
    }
}

@Data
class LayuiResult {
    Integer code;
    String msg;
    LayuiData data;
}

@Data
class LayuiData {
    String src;
    String title;
}