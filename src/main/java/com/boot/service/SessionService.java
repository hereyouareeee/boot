package com.boot.service;

import com.boot.common.constants.CacheConstant;
import com.boot.common.constants.SessionKeysConstant;
import com.boot.entity.SessionUserInfo;
import com.boot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
@Slf4j
public class SessionService {

    @Autowired
    RedisUtil redisUtil;
    /**
     * 获取用户信息
     * @return
     */
    public Optional<SessionUserInfo> getUser() {
        String sessionId = sessionId();
        if(sessionId == null) return Optional.empty();
        log.info("SESSION_ID: {}",sessionId);
        SessionUserInfo info = (SessionUserInfo) redisUtil
                .get(CacheConstant.SESSION_KEY_PREFIX + sessionId,
                        SessionUserInfo.class);
        return Optional.ofNullable(info);

    }

    /**
     * 获取sessionId
     * @return
     */
    public static  @NotNull String sessionId() {
        String sessionId = (String) getRequest().getAttribute(SessionKeysConstant.SESSION_KEY);
        return sessionId;
    }

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
