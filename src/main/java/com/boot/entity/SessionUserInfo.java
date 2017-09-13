package com.boot.entity;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SessionUserInfo implements Serializable{
    private Long userCode;
    private String mobile;
    private String name;
}
