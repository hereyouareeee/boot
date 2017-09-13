package com.boot.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "user") //实体类和表名对应的时候不用@table也可以
public class User{

    @Id
    private Long userCode;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "login_password")
    private String loginPassword;

    @Column(name = "has_login_password")
    private Integer hasLoginPassword;

    @Column(name = "hand_password")
    private String handPassword;

    @Column(name = "has_hand_password")
    private String hasHandPassword;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "is_old_user")
    private String isOldUser;

    @Column(name = "user_style")
    private Integer userStyle;

    @Column(name = "valid_control")
    private Integer validControl;

    @Column(name = "sync_flag")
    private String syncFlag;

    @Column(name = "is_deleted")
    private Integer isDeleted;

    public User(){}

    public User build(){
        User user=new User();
        user.setUserCode(this.userCode);
        user.setMobileNumber(this.mobileNumber);
        return user;
    }
    public static User builder(){
        return new User();
    }
    public User userCode(Long userCode){
        this.userCode=userCode;
        return this;
    }
    public User mobileNumber(String mobileNumber){
        this.mobileNumber=mobileNumber;
        return this;
    }
}
