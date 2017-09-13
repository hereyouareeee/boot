package com.boot.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class HttpClientUtilsTest {
    @Test
    public void postJson() throws Exception {
        Map map=new HashMap();
        map.put("userCode",1002121276);
        String post=HttpClientUtils.postJson("http://127.0.0.1:9999/boot/user/checkUser",map);
        log.info("this is post:{}",post);
    }

}