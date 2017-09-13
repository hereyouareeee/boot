package com.boot.exception.handler;

import com.boot.common.model.ResourceEntity;
import com.boot.common.model.ResourceEntityBuilder;
import com.boot.exception.BaseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class CustomExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    private ObjectMapper jsonMapper=new ObjectMapper();

    @ExceptionHandler(value = { BaseException.class })
    public final ResponseEntity<ResourceEntity> handleServiceException(BaseException ex,
                                                                       HttpServletRequest request) throws JsonProcessingException {
        // 注入servletRequest，用于出错时打印请求URL与来源地址
        logError(ex,request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        Integer flag;
        try {
            flag = Integer.parseInt(ex.errorCode.code());
        } catch (Exception e) {
            flag = 0;
        }
        return new ResponseEntity(ResourceEntityBuilder.fail(ex.getMessage(), flag), headers, HttpStatus.OK);
    }

    private Map<String, String> logError(Exception ex, HttpServletRequest request) throws JsonProcessingException {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", ex.getMessage());
        map.put("from", request.getRemoteAddr());
        String queryString = request.getQueryString();
        map.put("path", queryString != null ? (request.getRequestURI() + "?" + queryString)
                : request.getRequestURI());
        logger.error(jsonMapper.writeValueAsString(map));

        return map;
    }
    @ExceptionHandler(value = Exception.class)
    public final ResponseEntity<ResourceEntity> handleException(
            HttpServletRequest request, Exception ex) throws JsonProcessingException {
        logger.error("System error info: ");
        logError(ex,request);
        logger.error("System error stack info: {}", Throwables.getStackTraceAsString(ex));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
        return new ResponseEntity(ResourceEntityBuilder.fail(), headers, HttpStatus.OK);
    }



    public void logError(Exception ex) throws JsonProcessingException {
        Map<String, String> map = Maps.newHashMap();
        map.put("message", ex.getMessage());
        logger.error(jsonMapper.writeValueAsString(map), ex);
    }
}
