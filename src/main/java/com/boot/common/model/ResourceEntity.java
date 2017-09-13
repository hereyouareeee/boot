/*
 * Copyright (c) 2014-2015 Csyy, Inc. All Rights Reserved.
 */

package com.boot.common.model;


import com.boot.common.enums.HttpStatus;
import com.boot.util.CustomJsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects.ToStringHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.boot.common.model.ResourceEntityBuilder.builder;


@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceEntity<T> {

    @ApiModelProperty(value = "e.g. 1-success,0-error", required = true)
    @JsonProperty
    @JsonSerialize(using = CustomJsonSerialize.AnySerialize.class)
    @NotNull
    private Integer flag;

    @ApiModelProperty(value = "readable message corresponding to the return code", required = true)
    @JsonProperty
    @NotNull
    private String msg;

    @ApiModelProperty(value = "data returned to client, in JSON format", required = false)
    @JsonProperty
    @Valid
    private T data;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResourceEntity<T> other = (ResourceEntity<T>) o;

        return Objects.equals(this.getFlag(), other.getFlag())
                && Objects.equals(this.getMsg(), other.getMsg())
                && Objects.equals(this.getData(), other.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.getFlag(),
                this.getData(),
                this.getMsg());
    }

    public static <T> ResourceEntity<T> ok(Integer code, String message) {
        return builder()
                .flag(code)
                .msg(message)
                .build();
    }

    public static <T> ResourceEntity<T> ok(String message) {
        return ok(Integer.valueOf(HttpStatus.OK.getCode()), message);
    }

    public static <T> ResourceEntity<T> ok(T body) {

        return builder()
                .flag(Integer.valueOf(HttpStatus.OK.getCode()))
                .data(body)
                .msg(HttpStatus.OK.getMsg())
                .build();
    }

    @SuppressWarnings("unchecked")
    public static <T>  ResourceEntity<T> ok() {
        return builder()
                .flag(Integer.valueOf(HttpStatus.OK.getCode()))
                .msg(HttpStatus.OK.getMsg())
                .build();
    }
    @Override
    public String toString() {
        return toStringHelper().toString();
    }

    protected ToStringHelper toStringHelper() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("code", this.getFlag())
                .add("message", this.getMsg())
                .add("data", this.getData());
    }
    public static <T> ResourceEntity<T> error(String message){
        return builder().flag(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.getCode())).msg(message).build();
    }
    public static <T> ResourceEntity<T> error(Integer code,String message){
        return builder().flag(code).msg(message).build();
    }

    public static <T> ResourceEntity<T> error(Integer code,String message,T data){
        return builder().flag(code).msg(message).data(data).build();
    }
    public static <T> ResourceEntity<T> error(HttpStatus type){
        return error(Integer.valueOf(type.getCode()),type.getMsg());
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.getFlag()!=null&&this.getFlag()==1;
    }

}
