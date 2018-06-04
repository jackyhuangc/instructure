package com.jacky.strive.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/24 18:14
 * @since jdk1.8
 */
@Data
@Document(indexName = "strivetest1", type = "employee", shards = 1, replicas = 0, refreshInterval = "-1")
public class Employee {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    @JsonProperty("first_name")
    private String firstName;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Long)
    private Long age = 0L;

    @Field(type = FieldType.Text)
    private String about;
}