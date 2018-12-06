package com.wolf.controller;


import com.alibaba.fastjson.JSONObject;
import com.wolf.api.MemberApi;
import com.wolf.base.RestCommonService;
import com.wolf.base.Result;
import com.wolf.dao.MemberDao;
import com.wolf.entity.UserEntity;
import com.wolf.service.MemberService;
import com.wolf.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers) {
        memberService.register(user, headers);
        return Result.OK("用户注册成功");
    }

    /**
     * 用户名密码登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers) {
        JSONObject token = memberService.login(user, headers);
        return Result.OK("用户登录成功", token);
    }

    /**
     * token登录
     */
    @PostMapping("/findUserByToken")
    public Result findUserByToken(@RequestParam String token, @RequestHeader HttpHeaders headers) {
        UserEntity userEntity = memberService.findUserByToken(token, headers);
        return Result.OK(userEntity);
    }

    /**
     * 根据openid登录
     */
    @GetMapping("/findUserByOpenId")
    public Result findUserByOpenId(@RequestParam("openId") String openId, @RequestHeader HttpHeaders headers) {
        JSONObject token = memberService.findUserByOpenId(openId, headers);
        return Result.OK(token);
    }

    /**
     * openid和userId关联
     */
    @PostMapping("/qqRelation")
    public Result qqRelation(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers) {
        JSONObject token = memberService.openidRelationUserId(user, headers);
        return Result.OK(token);
    }

}