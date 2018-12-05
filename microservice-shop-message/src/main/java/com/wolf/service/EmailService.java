package com.wolf.service;


import com.alibaba.fastjson.JSONObject;
import com.wolf.adapter.MessageAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements MessageAdapter {

    @Value("${msg.from}")
    private String from;

    @Value("${msg.subject}")
    private String subject;

    @Value("${msg.text}")
    private String text;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMsg(JSONObject body) {
        String username = body.getString("username");
        String email = body.getString("email");
        if (StringUtils.isEmpty(email)) {
            return;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 发送
        simpleMailMessage.setFrom(from);
        // 接收
        simpleMailMessage.setTo(email);
        // 标题
        simpleMailMessage.setSubject(subject);
        // 内容
        simpleMailMessage.setText(text.replace("{}", username));
        mailSender.send(simpleMailMessage);
    }

}