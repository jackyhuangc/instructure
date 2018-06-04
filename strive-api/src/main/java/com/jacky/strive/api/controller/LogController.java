package com.jacky.strive.api.controller;

import com.github.pagehelper.PageInfo;
import com.jacky.strive.service.SystemLogService;
import com.jacky.strive.service.dto.PageDto;
import com.jacky.strive.service.dto.SystemLogPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qsq.biz.common.util.JsonUtil;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/6/2 21:57
 * @since jdk1.8
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    SystemLogService systemLogService;

    @GetMapping("")
    public PageInfo query(int page, int size, String operator, String content) {

        SystemLogPageDto pageDto = new SystemLogPageDto();
        pageDto.setPage(page);
        pageDto.setSize(size);
        pageDto.setOperator(operator);
        pageDto.setContent(content);

        PageInfo pageInfo = systemLogService.findSystemLog(pageDto);

        return pageInfo;
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") String id) {
        return String.format("您输入的id：%s", id);
    }
}
