package com.jacky.strive.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/6/3 20:10
 * @since jdk1.8
 */
@Data
public class PageDto {

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;
}
