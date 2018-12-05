package com.wolf.controller;


import com.alibaba.fastjson.JSONObject;
import com.wolf.base.Result;
import com.wolf.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public Result sendMsg(@RequestBody JSONObject body) {
        emailService.sendMsg(body);
        return Result.OK("邮件发送成功");
    }
}