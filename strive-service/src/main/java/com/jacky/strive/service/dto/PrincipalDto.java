package com.jacky.strive.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/6/7 16:53
 * @since jdk1.8
 */
@Data
public class PrincipalDto<T> {

    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("data")
    private T data;

    @JsonProperty("roles")
    private List<String> roles;
}
