package com.wolf.constants;

/**
 * 系统常量类
 *
 * @Author: wolf
 * @Date: 2018/12/3 20:07
 */
public interface Constants {

    /** 响应成功 **/
    String HTTP_RES_CODE_200_VALUE = "success";

    /** 系统错误 **/
    String HTTP_RES_CODE_500_VALUE = "fail";

    /** 响应成功code **/
    Integer HTTP_RES_CODE_200 = 200;

    /** 系统错误code **/
    Integer HTTP_RES_CODE_500 = 500;

    /** Member token前缀 **/
    String TOKEN_MEMBER = "TOKEN_MEMBER";

    /** 用户登录有效期30天 **/
    Long TOKEN_MEMBER_TIME = (long)(60 * 60 * 24 * 30);
}