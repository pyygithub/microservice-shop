package com.wolf.service;


import com.alibaba.fastjson.JSONObject;
import com.wolf.base.RestCommonService;
import com.wolf.base.Result;
import com.wolf.dao.MemberDao;
import com.wolf.entity.UserEntity;
import com.wolf.exception.MemberException;
import com.wolf.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@Slf4j
public class MemberService {

    private static final String MICROSERVICE_SHOP_RABBITMQ_HTTP_PREFIX = "http://microservice-shop-rabbitmq"; //服务名称

    private static final String MESSAGE_SECNE = "会员注册";// 场景名称

    @Value("${spring.application.name")
    private String domainName;

    @Autowired
    private RestCommonService restCommonService;

    @Autowired
    private MemberDao memberDao;

    /**
     * 会员注册
     *
     * @param user
     * @param headers
     */
    public void register(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers) {
        // 参数验证
        String password = user.getPassword();
        if (StringUtils.isBlank(password)) {
            throw new MemberException("密码不能为空");
        }

        // 密码MD5简单加密
        user.setPassword(MD5Util.MD5(password));
        int res = memberDao.insertUser(user);
        if (res <= 0) {
            throw new MemberException("用户注册失败");
        }

        // 向Rabbitmq推送消息
        sendMsg(user, headers);
    }

    private void sendMsg(UserEntity user, HttpHeaders headers) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("messageCode", "email_msg");
            msg.put("email", user.getEmail());
            msg.put("username", user.getUsername());

            log.info("####会员服务推送消息到Rabbitmq#####");
            log.info("####消息内容：msg={}", msg);
            String url = MICROSERVICE_SHOP_RABBITMQ_HTTP_PREFIX + "/v1/rabbitmq/sendToDefaultExchange?domainName={domainName}&messageSecne={messageSecne}";
            restCommonService.post(headers, msg, url, domainName, MESSAGE_SECNE);
        } catch (Exception e) {
            log.error("####会员推送消息异常####, e={}", e);
        }
    }
}