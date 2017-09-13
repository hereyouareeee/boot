package com.boot.web;

import com.boot.common.base.Auth;
import com.boot.common.constants.SessionKeysConstant;
import com.boot.common.model.ResourceEntity;
import com.boot.common.model.ResourceEntityBuilder;
import com.boot.entity.SessionUserInfo;
import com.boot.entity.User;
import com.boot.entity.view.UserView;
import com.boot.service.SessionService;
import com.boot.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "用户信息",description = "用户信息")
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    SessionService sessionService;
    @Auth
    @RequestMapping(value = "checkUser",method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation("获取用户手机号")
    public ResourceEntity<UserView> getUserView(){
        SessionUserInfo userInfo=sessionService.getUser().get();
        User user=User.builder().userCode(userInfo.getUserCode()).mobileNumber(userInfo.getMobile()).build();
        return userService.checkUser(userInfo.getUserCode());
    }
}
