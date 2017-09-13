/*
 * Copyright (c) 2014-2015 Huami, Inc. All Rights Reserved.
 */

package com.boot.common.model;

import com.boot.common.enums.HttpStatus;

public class ResourceEntityBuilder<T> {
    private Integer flag;

    private String msg;

    private T data;

    public ResourceEntityBuilder() {
    }

    public static <T> ResourceEntity<T> reponseError(String message) {
        return new ResourceEntityBuilder<T>().flag(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.getCode()))
                .msg(message).build();
    }

    public ResourceEntity<T> build() {
        ResourceEntity<T> result = new ResourceEntity<>();
        result.setFlag(this.flag);
        result.setMsg(this.msg);
        result.setData(this.data);
        return result;
    }

    public ResourceEntityBuilder<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResourceEntityBuilder<T> flag(Integer flag) {
        this.flag = flag;
        return this;
    }

    public static <T> ResourceEntity<T> success(String message) {
        return new ResourceEntityBuilder<T>().flag(Integer.valueOf(HttpStatus.OK.getCode()))
                .msg(message).build();
    }


    public static <T> ResourceEntityBuilder builder() {
        return new ResourceEntityBuilder<T>();
    }

    public ResourceEntityBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> ResourceEntity<T> fail() {
        return fail(HttpStatus.INTERNAL_SERVER_ERROR.getMsg());
    }
    public static <T> ResourceEntity<T> fail(String message) {
        return fail(message,Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.getCode()));
    }

    public static <T> ResourceEntity<T> fail(String message, Integer flag) {
        return builder()
                .flag(flag)
                .msg(message)
                .build();
    }


    public static <T> ResourceEntity<T> reponseError(String message,String flag) {
        return new ResourceEntityBuilder<T>().flag(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.getCode()))
                .msg(message).flag(Integer.valueOf(flag)).build();
    }
}
