package com.wolf.member.api;


import com.wolf.base.Result;
import com.wolf.member.entity.UserEntity;
import com.wolf.common.base.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 会员服务接口API
 *
 * @Author: wolf
 * @Date: 2018/12/4 14:12
 */
@FeignClient("microservice-shop-member")
public interface MemberApi {

    @PostMapping("/register")
    public Result register(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers);
}