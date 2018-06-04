package com.jacky.strive.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/6/2 22:57
 * @since jdk1.8
 */
@Data
@Document(indexName = "strive", type = "system_log", shards = 1, replicas = 0, refreshInterval = "-1")
public class SystemLog {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    @JsonProperty("operator")
    private String operator;

    @Field(type = FieldType.Date)
    @JsonProperty("opt_time")
    private Date optTime;

    /**
     * ik_max_word: 会将文本做最细粒度的拆分，比如会将“中华人民共和国国歌”拆分为“中华人民共和国”、“中华人民”、“中华”、“华人”、“人民共和国”、“人民”、“人”、“民”,、“共和国”、“共和”、“和”、“国歌”等，会穷尽各种可能的组合；
     * ik_smart: 会做最粗粒度的拆分，比如会将“中华人民共和国国歌”拆分为“中华人民共和国”、“国歌”。
     */
    @Field(type = FieldType.Text, fielddata = true, searchAnalyzer = "ik_max_word", analyzer = "ik_max_word")
    @JsonProperty("opt_content")
    private String optContent;
}
