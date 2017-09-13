package com.boot.service;

import com.boot.entity.view.UserView;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    public void checkUser() throws Exception {
        UserView userView=(UserView) userService.checkUser(1002121276L).getData();
        assertThat(userView).isNotNull().hasFieldOrPropertyWithValue("mobileNumber","18310020729");
    }

}