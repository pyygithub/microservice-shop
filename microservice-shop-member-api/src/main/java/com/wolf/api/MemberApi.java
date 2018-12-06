package com.wolf.api;


import com.wolf.base.Result;
import com.wolf.entity.UserEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 会员服务接口API
 *
 * @Author: wolf
 * @Date: 2018/12/4 14:12
 */
@RequestMapping("/member")
public interface MemberApi {

    @PostMapping("/register")
    public Result register(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers);

    @PostMapping("/login")
    public Result login(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers);

    @PostMapping("/findUserByToken")
    public Result findUserByToken(@RequestParam("token") String token, @RequestHeader HttpHeaders headers);
}