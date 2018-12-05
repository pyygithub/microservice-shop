package com.wolf.member.api.impl;


import com.alibaba.fastjson.JSONObject;


import com.wolf.common.base.Result;
import com.wolf.common.utils.MD5Util;
import com.wolf.common.utils.RestCommonService;
import com.wolf.member.api.MemberApi;
import com.wolf.member.dao.MemberDao;
import com.wolf.member.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MemberApiImpl implements MemberApi {

    private static final String MICROSERVICE_SHOP_RABBITMQ_HTTP_PREFIX = "http://microservice-shop-rabbitmq"; //服务名称

    private static final String MESSAGE_SECNE = "会员注册";// 场景名称

    @Value("${spring.application.name")
    private String domainName;

    @Autowired
    private RestCommonService restCommonService;

    @Autowired
    private MemberDao memberDao;

    @Override
    public Result register(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers) {
        // 参数验证
        String password = user.getPassword();
        if (StringUtils.isBlank(password)) {
            return Result.ERROR("密码不能为空");
        }

        // 密码MD5简单加密
        user.setPassword(MD5Util.MD5(password));
        int res = memberDao.insertUser(user);
        if (res <= 0) {
            return Result.ERROR("用户注册失败");
        }

        // 向Rabbitmq推送消息
        sendMsg(user, headers);

        return Result.OK("用户注册成功");
    }

    private void sendMsg(UserEntity user, HttpHeaders headers) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("messageCode", "email_msg");
            msg.put("content", user.getEmail());

            log.info("####会员服务推送消息到Rabbitmq#####");
            log.info("####消息内容：msg={}", msg);
            String url = MICROSERVICE_SHOP_RABBITMQ_HTTP_PREFIX + "/v1/rabbitmq/sendToDefaultExchange?domainName={domainName}&messageSecne={messageSecne}";
            restCommonService.post(headers, msg, url, domainName, MESSAGE_SECNE);
        } catch (Exception e) {
            log.error("####会员推送消息异常####, e={}", e);
        }
    }
}