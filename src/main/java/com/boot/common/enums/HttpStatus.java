package com.boot.common.enums;

public enum HttpStatus {
    OK("1","成功"),
    INTERNAL_SERVER_ERROR("0","服务器请求繁忙，请稍后重试"),
    IS_NOT_NULL("2","用户名或密码不能为空"),
    UNAUTHORIZED("3","没有权限"),
    USER_NOT_FOUND("4","用户不存在"),
    PASSWD_ERROR("5","密码错误"),
    USER_NOT_LOGIN("-1","未登录或登录超时,请重新登录!");
    private  String code;
    private  String msg;

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
    HttpStatus(String code,String msg){
        this.code=code;
        this.msg=msg;
    }
}
