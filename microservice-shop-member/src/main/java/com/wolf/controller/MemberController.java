package com.wolf.controller;


import com.alibaba.fastjson.JSONObject;
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

    @PostMapping("/register")
    public Result register(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers) {
        memberService.register(user, headers);
        return Result.OK("用户注册成功");
    }

}