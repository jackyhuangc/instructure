package com.jacky.strive.api.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import qsq.biz.scheduler.entity.ResResult;


@CrossOrigin() // 需支持跨域,否则客户端虽能正常访问，但也会报跨域异常
@RestController
public class CommonController {

	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	/**
	 * 文件上传具体实现方法（单文件上传）
	 *
	 * @param file
	 * @return
	 * 
	 * @author 单红宇(CSDN CATOOP)
	 * @create 2017年3月11日
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public ResResult<String> upload(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				// 这里只是简单例子，文件直接输出到项目路径下。
				// 实际项目中，文件需要输出到指定位置，需要在增加代码处理。
				// 还有关于文件格式限制、文件大小限制，详见：中配置。

				// 这是绝对路径
				// log.info(this.getClass().getResource("/").getPath());

				// 相对路径
				String fileDir = "upload/";// 文件夹路径
				File myPath = new File(fileDir);
				if (!myPath.exists()) {// 若此目录不存在，则创建之
					myPath.mkdir();
					logger.info("创建文件夹路径为：" + fileDir);
				}

				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File(fileDir + file.getOriginalFilename())));
				out.write(file.getBytes());
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				//return new ReturnResult<String>("001", e.getMessage(), "");
				return ResResult.fail(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();

				//return new ReturnResult<String>("001", e.getMessage(), "");
				return ResResult.fail(e.getMessage());
			}

			//return new ReturnResult<String>("000", "", "/upload/" + file.getOriginalFilename());

			return ResResult.success("上传成功","/upload/" + file.getOriginalFilename());
		} else {

			//return new ReturnResult<String>("001", "empty file", "");
			return ResResult.fail("empty file");
		}
	}

	/**
	 * 多文件上传 主要是使用了MultipartHttpServletRequest和MultipartFile
	 *
	 * @param request
	 * @return
	 * 
	 * @author 单红宇(CSDN CATOOP)
	 * @create 2017年3月11日
	 */
	@RequestMapping(value = "/upload/batch", method = RequestMethod.POST)
	public @ResponseBody
	ResResult<List<String>> batchUpload(HttpServletRequest request) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		MultipartFile file = null;
		BufferedOutputStream stream = null;

		// 相对路径
		String fileDir = "upload/";// 文件夹路径
		File myPath = new File(fileDir);
		if (!myPath.exists()) {// 若此目录不存在，则创建之
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
				} catch (Exception e) {
					stream = null;

					//return new ReturnResult<List<String>>("001", , null);
					return ResResult.fail(i + "*" + e.getMessage());
				}
			} else {
//				return new ReturnResult<List<String>>("001",
//						"You failed to upload " + i + " because the file was empty.", null);
				return ResResult.fail("You failed to upload " + i + " because the file was empty.");
			}
		}

		return ResResult.success("",listFilePath);//new ReturnResult<List<String>>("000", "", listFilePath);
	}

	private final ResourceLoader resourceLoader;

	@Autowired
	public CommonController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	// 显示图片的方法关键 匹配路径像 localhost:8080/b7c76eb3-5a67-4d41-ae5c-1642af3f8746.png
	@RequestMapping(method = RequestMethod.GET, value = "/upload/{filename:.+}")
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable String filename) {

		try {
			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get("upload", filename).toString()));
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
}