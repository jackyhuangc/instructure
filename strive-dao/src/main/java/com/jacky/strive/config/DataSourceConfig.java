package com.jacky.strive.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = {"classpath:jdbc.properties", "classpath:application.properties"})
@ComponentScan(basePackages = {"com.jacky"})
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
// 扫描 Mapper 接口并容器管理 FIXME 若使用tk.mybatis,请换成tk.xxxx的@MapperScan，但是tk的@MapperScan会报No MyBatis mapper was found in 警告，需特殊处理
@MapperScan(basePackages = {"com.jacky.strive.dao"}, sqlSessionFactoryRef = "striveSqlSessionFactory")
public class DataSourceConfig {

    /**
     * 精确到 数据源 目录，以便跟其他数据源隔离
     */
    //static final String[] MAPPER_LOCATION = {"classpath:mapper/strive/*.xml", "classpath*:mybatis/dao/*.xml"};
    static final String[] MAPPER_LOCATION = {"classpath*:mapper/strive/*.xml", "classpath*:mybatis/dao/*.xml"};
    @Value("${strive.datasource.url}")
    private String url;

    @Value("${strive.datasource.username}")
    private String user;

    @Value("${strive.datasource.password}")
    private String password;

    @Value("${strive.datasource.driverClassName}")
    private String driverClass;

    @Value("${jdbc.initialSize}")
    private int initialSize;

    @Value("${jdbc.minIdle}")
    private int minIdle;

    @Value("${jdbc.maxActive}")
    private int maxActive;

    @Value("${jdbc.maxWait}")
    private int maxWait;

    @Value("${jdbc.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${jdbc.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${jdbc.validationQuery}")
    private String validationQuery;

    @Value("${jdbc.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${jdbc.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${jdbc.testOnReturn}")
    private boolean testOnReturn;

    @Value("${jdbc.removeAbandoned}")
    private boolean removeAbandoned;

    @Value("${jdbc.removeAbandonedTimeout}")
    private int removeAbandonedTimeout;

    @Value("${jdbc.logAbandoned}")
    private boolean logAbandoned;

    @Value("${jdbc.maxOpenPreparedStatements}")
    private int maxOpenPreparedStatements;

    @Bean(name = "striveDataSource")
    @Primary
    public DataSource striveDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        // FIXME 可对MYSQL账号密码进行加密
        //dataSource.setUsername(new String(Base64.decodeBase64(user)));
        //dataSource.setPassword(new String(Base64.decodeBase64(password)));
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setRemoveAbandoned(removeAbandoned);
        dataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        dataSource.setLogAbandoned(logAbandoned);
        dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        return dataSource;
    }

    @Bean(name = "striveTransactionManager")
    @Primary
    public DataSourceTransactionManager striveTransactionManager() {
        return new DataSourceTransactionManager(striveDataSource());
    }

    @Bean(name = "striveSqlSessionFactory")
    @Primary
    public SqlSessionFactory striveSqlSessionFactory(@Qualifier("striveDataSource") DataSource reconDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(reconDataSource);
        Resource[] resourcesStrive = new PathMatchingResourcePatternResolver()
                .getResources(DataSourceConfig.MAPPER_LOCATION[0]);
        Resource[] resourcesTask = new PathMatchingResourcePatternResolver()
                .getResources(DataSourceConfig.MAPPER_LOCATION[1]);
        Resource[] resources = ArrayUtils.addAll(resourcesStrive, resourcesTask);
        sessionFactory.setMapperLocations(resources);
        return sessionFactory.getObject();
    }
}