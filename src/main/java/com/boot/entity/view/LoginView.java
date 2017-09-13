package com.boot.entity.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录信息")
public class LoginView {

    @ApiModelProperty("用户名")
    public String userName;
    @ApiModelProperty("密码")
    public String password;
}
