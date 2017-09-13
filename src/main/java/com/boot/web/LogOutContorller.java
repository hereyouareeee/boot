package com.boot.web;

import com.boot.common.base.Auth;
import com.boot.common.model.ResourceEntity;
import com.boot.service.LogOutService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "退出",tags = "退出登录")
@RequestMapping("logOut")
public class LogOutContorller {

    @Autowired
    LogOutService logOutService;

    @Auth
    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "doLogOut",method = {RequestMethod.GET,RequestMethod.POST})
    public ResourceEntity logout(@ApiParam(value = "sessionId",required = true)
                                     @RequestParam String sessionId, HttpServletRequest request){
        return logOutService.logout(sessionId,request);
    }
}
