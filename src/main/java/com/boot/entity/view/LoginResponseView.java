package com.boot.entity.view;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Api(tags = "登录返回数据",value = "登录信息")
@Builder
@Getter
public class LoginResponseView {

    @ApiModelProperty("sessionId")
    private String sessionId;
    @ApiModelProperty("用户id")
    private String userCode;
}
