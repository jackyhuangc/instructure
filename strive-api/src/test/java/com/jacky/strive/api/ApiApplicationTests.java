package com.jacky.strive.api;

import com.jacky.strive.api.controller.HomeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void testLogSentry()
	{
		Logger logger = LoggerFactory.getLogger(HomeController.class);
		logger.warn("我的XXXXXXXXXsentry错误测试。。。");
	}
}
