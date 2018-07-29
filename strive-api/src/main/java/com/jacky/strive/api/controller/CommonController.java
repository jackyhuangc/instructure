package com.jacky.strive.api.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jacky.strive.common.LogUtil;
import com.jacky.strive.service.dto.PostEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import qsq.biz.common.util.StringUtil;
import qsq.biz.scheduler.entity.ResResult;


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

    @RequestMapping(value = "/proxy/post", method = RequestMethod.POST)
    @ResponseBody
    public String ProxyPost(@RequestBody PostEntityDto postEntity) {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> httpEntity;

        if (postEntity.getHeader() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", postEntity.getHeader());
            httpEntity = new HttpEntity<Object>(postEntity.getData(), headers);
        } else {
            httpEntity = new HttpEntity<Object>(postEntity.getData());
        }

        String restResult = restTemplate.postForObject(postEntity.getUrl(), httpEntity, String.class);
        return restResult;
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
}