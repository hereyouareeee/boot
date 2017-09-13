package com.boot.service;

import com.boot.common.constants.CacheConstant;
import com.boot.common.constants.SessionKeysConstant;
import com.boot.common.model.ResourceEntity;
import com.boot.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogOutService {

    @Autowired
    RedisUtil redisUtil;

    public ResourceEntity logout(String sessionId, HttpServletRequest request){
        request.getSession().removeAttribute(SessionKeysConstant.SESSION_KEY);
        request.getSession().invalidate();
        redisUtil.delete(CacheConstant.SESSION_KEY_PREFIX+sessionId);
        return ResourceEntity.ok();
    }
}
