package com.jacky.strive.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.dao.model.User;
import com.jacky.strive.service.dto.PageDto;
import com.jacky.strive.service.dto.SystemLogPageDto;
import com.jacky.strive.service.model.SystemLog;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import qsq.biz.common.util.JsonUtil;
import qsq.biz.common.util.LogUtil;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

// FIXME ELK(ElasticSearch+Logstash+ Kibana)搭建实时日志分析平台
// FIXME 既然有Kibana作日志，为什么还要单独写接口提供日志，有必要吗？？？？日志只需包含业务即可，如资金流水
/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/5 21:09
 * @since jdk1.8
 */
@Service
@Scope("prototype")
public class SystemLogService {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    SystemLogRepository systemLogRepository;

    public PageInfo<SystemLog> findSystemLog(SystemLogPageDto pageDto) {

        SystemLog log = new SystemLog();
        log.setOperator("jk");
        log.setOptTime(new Date());
        log.setOptContent("测试数据内容");
        systemLogRepository.save(log);

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        //builder下有must、should以及mustNot 相当于sql中的and、or以及not

        //设置模糊搜索(wildcardQuery通配符查询)
        builder.must(QueryBuilders.wildcardQuery("operator", String.format("*%s*", pageDto.getOperator())));
        builder.must(QueryBuilders.wildcardQuery("opt_content", String.format("*%s*", pageDto.getContent())));

        // https://blog.csdn.net/qq_35280509/article/details/51637726 中文查询处理
//        // 查询条件
//        String type = "system";
//        String indexname = "logstash-2016-06-11";
//        QueryBuilder queryBuilder = QueryBuilders.termQuery("message", "测");
//        QueryBuilder queryBuilder2 = QueryBuilders.termQuery("message", "试");
//
//        BoolQueryBuilder bool = QueryBuilders.boolQuery();
//        bool.must(queryBuilder);
//        bool.must(queryBuilder2);
//        esHandler.searcher(bool, indexname,type);
        //builder.must(QueryBuilders.fuzzyQuery("opt_content", content)); // 这是按分词查询？？？

        //按照操作时间倒序
        FieldSortBuilder sort = SortBuilders.fieldSort("opt_time").order(SortOrder.DESC);

        //注意!es的分页api是从第0页开始的(坑)
        PageRequest page = PageRequest.of(pageDto.getPage() - 1, pageDto.getSize());

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //将条件设置到构建中
        nativeSearchQueryBuilder.withQuery(builder);
        //将排序设置到构建中
        nativeSearchQueryBuilder.withSort(sort);
        //将分页设置到构建中
        nativeSearchQueryBuilder.withPageable(page);

        //生产NativeSearchQuery
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        org.springframework.data.domain.Page<SystemLog> search = systemLogRepository.search(query);

        long total = search.getTotalElements();
        List<SystemLog> systemLogList = search.getContent();

        PageInfo pageInfo = new PageInfo<SystemLog>();
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageDto.getPage());
        pageInfo.setPageSize(pageDto.getSize());
        pageInfo.setList(systemLogList);

        return pageInfo;
    }
}
