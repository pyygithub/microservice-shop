package com.wolf.api.service;

import com.wolf.base.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/member")
public interface TestMemberApiService {

    @GetMapping("/test/api")
    public Result test(Integer id);

    @GetMapping("/setRedisTest")
    public Result setRedisTest(String key, String value);

    @GetMapping("/getRedisTest")
    public Result getRedisTest(String key);
}
