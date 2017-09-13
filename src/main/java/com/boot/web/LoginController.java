package com.boot.web;

import com.boot.common.model.ResourceEntity;
import com.boot.entity.view.LoginView;
import com.boot.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "登录",tags = "登录接口")
@RequestMapping("login")
public class LoginController {

    @Autowired
    LoginService loginService;


    @RequestMapping(value = "doLogin",method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "登录")
    public ResourceEntity login(
            @ApiParam(value = "登录信息",required = true) @RequestBody LoginView loginView,
            HttpServletRequest request){
        return loginService.login(loginView,request);
    }
}
