package com.jacky.strive.service;

import com.jacky.strive.service.model.SystemLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/24 18:23
 * @since jdk1.8
 */
@Component
public interface SystemLogRepository extends ElasticsearchRepository<SystemLog, String> {

}