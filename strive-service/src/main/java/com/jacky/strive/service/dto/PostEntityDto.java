package com.jacky.strive.service.dto;

public class PostEntityDto<T> {
    private String url;
    private String header;
    private T data;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}