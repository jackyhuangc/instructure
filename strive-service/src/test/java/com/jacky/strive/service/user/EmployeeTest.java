/*
package com.jacky.mybatis.dao.service.user;

import com.jacky.mybatis.dao.config.DataSourceConfig;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import qsq.biz.common.util.JsonUtil;
import qsq.biz.common.util.LogUtil;

import java.util.List;

*
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/24 18:25
 * @since jdk1.8


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataSourceConfig.class)
@SpringBootTest
public class EmployeeTest {
    @Autowired
    private EmployeeRepository er;

    @Test
    public void testAdd() {
        Employee employee = new Employee();
        employee.setId(String.valueOf(System.currentTimeMillis()));
        employee.setFirstName("jacky");
        employee.setLastName("zh");
        employee.setAge(26L);
        employee.setAbout("i am in peking");
        Employee ret = er.save(employee);

        employee.setId(String.valueOf(System.currentTimeMillis()));
        ret = er.save(employee);
    }

    @Test
    public void testQuery() {

        Employee accountInfo = er.queryEmployeeById("1");

        // 范围查询的builder
//        double lat = 39.929986;
//        double lon = 116.395645;
//
//        //查询某经纬度100米范围内
//        GeoDistanceQueryBuilder builder = QueryBuilders.geoDistanceQuery("address").point(lat, lon)
//                .distance(100, DistanceUnit.METERS);


        // 词条查询，exacttext，精确查询，不会分析
        //TermQueryBuilder builder1=QueryBuilders.termQuery()

        //创建builder(常用)
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 匹配查询(常用)，全文查询fulltext，会分析，拆分单词
        //MatchQueryBuilder builder2=QueryBuilders.matchQuery("",null).operator(Operator.AND).ma;

        //builder下有must、should以及mustNot 相当于sql中的and、or以及not
        //设置模糊搜索
        builder.must(QueryBuilders.fuzzyQuery("first_name", "jacky"));
        //设置性别必须为man
        //builder.must(new QueryStringQueryBuilder("man").field("sex"));

        //按照年龄从高到低
        FieldSortBuilder sort = SortBuilders.fieldSort("age").order(SortOrder.DESC);

        //设置分页(拿第一页，一页显示两条)
        //注意!es的分页api是从第0页开始的(坑)
        PageRequest page = PageRequest.of(0, 2);

        //构建查询
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //将搜索条件设置到构建中
        nativeSearchQueryBuilder.withQuery(builder);
        //将分页设置到构建中
        nativeSearchQueryBuilder.withPageable(page);
        //将排序设置到构建中
        nativeSearchQueryBuilder.withSort(sort);
        //生产NativeSearchQuery
        NativeSearchQuery query = nativeSearchQueryBuilder.build();

        Page<Employee> search = er.search(query);
        long total = search.getTotalElements();
        List<Employee> employeeList = search.getContent();

        LogUtil.warn(JsonUtil.toJson(employeeList));
        //System.err.println(new Gson().toJson(accountInfo));
    }

    @Test
    public void testDeleteAll() {
        er.deleteAll();
        LogUtil.warn("清除成功");
    }
}
*/
