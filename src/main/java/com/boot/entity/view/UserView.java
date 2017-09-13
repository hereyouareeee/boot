package com.boot.entity.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户信息")
public class UserView {

    @ApiModelProperty("用户id")
    private String userCode;

    @ApiModelProperty("手机号")
    private String mobileNumber;
}
