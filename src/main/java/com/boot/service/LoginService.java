package com.boot.service;

import com.boot.common.constants.CacheConstant;
import com.boot.common.constants.SessionKeysConstant;
import com.boot.common.enums.HttpStatus;
import com.boot.common.model.ResourceEntity;
import com.boot.entity.SessionUserInfo;
import com.boot.entity.User;
import com.boot.entity.view.LoginResponseView;
import com.boot.entity.view.LoginView;
import com.boot.repository.UserRepository;
import com.boot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class LoginService {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    UserRepository userRepository;
    @Value("${session.sessionId.timeout}")
    Long expireTime;
    public ResourceEntity login(LoginView view, HttpServletRequest request){
        if (StringUtils.isEmpty(view.getUserName()) || StringUtils.isEmpty(view.getPassword())){
            log.info("login fail:{}",HttpStatus.IS_NOT_NULL.getMsg());
            return ResourceEntity.error(HttpStatus.IS_NOT_NULL);
        }
        //验证密码
        User user= userRepository.findByMobileNumber(view.getUserName());
        if (DigestUtils.md5Hex(view.getPassword()).equals(user.getLoginPassword())){
            HttpSession session=request.getSession();
            SessionUserInfo userInfo=SessionUserInfo.builder().mobile(view.userName).userCode(user.getUserCode()).build();
            String sessionId=randomSession(session.getId(),view.userName);
            session.setAttribute(CacheConstant.SESSION_KEY_PREFIX + sessionId,userInfo);
            session.setAttribute(SessionKeysConstant.SESSION_KEY,sessionId);
            redisUtil.set(CacheConstant.SESSION_KEY_PREFIX + sessionId,userInfo,expireTime);//时间可以在拦截器里不停的刷新
            LoginResponseView responseView= LoginResponseView.builder().sessionId(sessionId).userCode(user.getUserCode().toString()).build();
            log.info("login success:{}",user.getUserCode()+"："+sessionId);
            return ResourceEntity.ok(responseView);
        }
        log.info("login fail :{}",user.getUserCode()+HttpStatus.PASSWD_ERROR.getMsg());
        return ResourceEntity.error(HttpStatus.PASSWD_ERROR);
    }
    // 防止sesssion互串，增加随机的sessionid处理
    public String randomSession(String sessionId, String key) {
        String returnSessionId = sessionId + key;
        returnSessionId = DigestUtils.md5Hex(returnSessionId);
        int loopIndex = 0;
        while (redisUtil.exist(returnSessionId)) {

            returnSessionId = returnSessionId + loopIndex;
            returnSessionId = DigestUtils.md5Hex(returnSessionId);
            loopIndex++;
            if (loopIndex > 1000) {
                log.info("sessionId generate:已经超过1000次循环");
            }
        }
        log.info("生成sessionId循环次数：" + loopIndex);
        return returnSessionId;
    }
}
