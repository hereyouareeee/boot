package com.boot.common.interceptor;

import com.boot.common.base.Auth;
import com.boot.common.constants.CacheConstant;
import com.boot.common.constants.SessionKeysConstant;
import com.boot.common.enums.HttpStatus;
import com.boot.entity.SessionUserInfo;
import com.boot.exception.ServiceException;
import com.boot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

import static com.boot.common.constants.SessionKeysConstant.USER_CODE_SESSION_KEY;

/**
 * @Auth
 * 登录拦截
 */
@Slf4j
@Component
public class AuthLoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisUtil redisUtils;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //判断响应的处理器是否为HandlerMethod类或者HandlerMethod的子类
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            return true;
        }
        handlerSession(request);
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Method method = handlerMethod.getMethod();
        final Class<?> clazz = method.getDeclaringClass();
        if (clazz.isAnnotationPresent(Auth.class) ||
                method.isAnnotationPresent(Auth.class)) {
            if(request.getAttribute(USER_CODE_SESSION_KEY) == null){
                throw new ServiceException(HttpStatus.USER_NOT_LOGIN);
            }else{
                return true;
            }
        }

        return true;

    }
    public void  handlerSession(HttpServletRequest request) {
//        /**
//         * 当swagger为BasicAuth模式时
//         */
//        String authorization=request.getHeader("Authorization");
//        if (StringUtils.isNotBlank(authorization)){
//            String base64Auth=authorization.replace("Basic","").trim();
//            String[] auth=new String(Base64.getDecoder().decode(base64Auth)).split(":");
//            String userName=auth[0];
//            String password=auth[1];
//            log.info("this is :{}",userName+"?"+password);
//            /**
//             * todo 处理:
//             * 可以在登录时将(userCode,sessionId)存入redis，
//             * 在此处根据userName查询出userCode，在redis取出sessionId
//             */
//        }
        String sessionId = request.getHeader(SessionKeysConstant.SESSION_KEY);
        if(StringUtils.isBlank(sessionId)){
            sessionId=(String) request.getSession().getAttribute(SessionKeysConstant.SESSION_KEY);
        }
        if (StringUtils.isNotBlank(sessionId)) {
            SessionUserInfo info= redisUtils.get(CacheConstant.SESSION_KEY_PREFIX+sessionId,SessionUserInfo.class);
            if (info == null) {
                return ;
            }
            request.setAttribute(SessionKeysConstant.SESSION_KEY,sessionId);
            Long userCode = info.getUserCode();
            if (userCode != null) {
                request.setAttribute(SessionKeysConstant.USER_CODE_SESSION_KEY, userCode);
            }
            String mobile = info.getMobile();
            if (mobile != null) {
                request.setAttribute(SessionKeysConstant.MOBILE_NUMBER_SESSION_KEY, mobile);
            }
        }
        return ;
    }
}

