package com.boot.repository;

import com.boot.entity.User;
import com.boot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisUtil redisUtil;

    @Test
    public void findByUserCode() throws Exception {
        User user=userRepository.findByUserCode(1001967253L);
        User user1=userRepository.findOne(1001967253L);
        log.info("this is count {}",userRepository.count());
    }


}