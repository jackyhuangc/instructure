package com.jacky.strive.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/6/3 20:21
 * @since jdk1.8
 */
@Data
public class SystemLogPageDto extends PageDto {

    @JsonProperty("operator")
    private String operator;

    @JsonProperty("content")
    private String content;
}
