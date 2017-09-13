package com.boot.service;

import com.boot.common.model.ResourceEntity;
import com.boot.entity.User;
import com.boot.entity.view.UserView;
import com.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;



import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Cacheable(value = "user",key = "'userInfo:'.concat(#userCode)")
    public ResourceEntity checkUser(Long userCode){
        User user=userRepository.findByUserCode(userCode);
        UserView view=new UserView();
        view.setMobileNumber(user.getMobileNumber());
        view.setUserCode(userCode.toString());
        return ResourceEntity.ok(view);
    }
}
