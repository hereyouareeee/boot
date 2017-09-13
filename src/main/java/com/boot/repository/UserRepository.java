package com.boot.repository;

import com.boot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserCode(Long userCode);

    List<User> findByCreatedDateIsBetween(Date start,Date end);

    User findByMobileNumber(String mobileNumber);
}
