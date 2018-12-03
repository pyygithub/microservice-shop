package com.wolf.api.service.impl;


import com.wolf.api.service.TestMemberApiService;
import com.wolf.base.BaseRedisService;
import com.wolf.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TestMemberApiServiceImpl implements TestMemberApiService{

    @Autowired
    private BaseRedisService baseRedisService;

    @Override
    public Result test(Integer id) {
        return Result.OK();
    }

    @Override
    public Result setRedisTest(String key, String value) {
        baseRedisService.setString(key, value);
        return Result.OK();
    }

    @Override
    public Result getRedisTest(String key) {
        String value = baseRedisService.getString(key);
        return Result.OK(value);
    }
}