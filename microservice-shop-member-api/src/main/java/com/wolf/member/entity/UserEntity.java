package com.wolf.member.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserEntity {
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Date created;
    private Date updated;
}