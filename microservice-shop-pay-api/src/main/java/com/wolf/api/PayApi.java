package com.wolf.api;

import com.alibaba.fastjson.JSONObject;
import com.wolf.base.Result;
import com.wolf.entity.PaymentInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pay")
public interface PayApi {

    // 创建支付令牌
    @PostMapping("/createToken")
    public Result getPayToken(@RequestBody PaymentInfo paymentInfo, @RequestHeader HttpHeaders headers);

    // 使用支付令牌查找支付信息
    @GetMapping("/findPayByToken")
    public Result payInfo(@RequestParam(value = "payToken", required = true) String payToken, @RequestHeader HttpHeaders headers);
}
