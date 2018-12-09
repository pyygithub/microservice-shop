package com.wolf.controller;

import com.alibaba.fastjson.JSONObject;
import com.wolf.api.PayApi;
import com.wolf.base.Result;
import com.wolf.entity.PaymentInfo;
import com.wolf.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PayController implements PayApi{

    @Autowired
    private PayService payService;

    @Override
    public Result getPayToken(@RequestBody PaymentInfo paymentInfo, @RequestHeader HttpHeaders headers) {
        JSONObject token = payService.getPayToken(paymentInfo, headers);

        return Result.OK(token);
    }

    @Override
    public Result payInfo(@RequestParam(value = "payToken", required = true) String payToken, @RequestHeader HttpHeaders headers) {
        return null;
    }

}