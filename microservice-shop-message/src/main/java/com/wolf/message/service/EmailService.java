package com.wolf.message.service;


import com.alibaba.fastjson.JSONObject;
import com.wolf.message.adapter.MessageAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements MessageAdapter {

    @Value("${msg.subject}")
    private String subject;

    @Value("${msg.text}")
    private String text;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendMsg(JSONObject body) {
        String mail = body.getString("mail");
        if (StringUtils.isEmpty(mail)) {
            return;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 发送
        simpleMailMessage.setFrom(mail);

        simpleMailMessage.setTo(mail);
        // 标题
        simpleMailMessage.setSubject(subject);
        // 内容
        simpleMailMessage.setText(text.replace("{}", mail));
        mailSender.send(simpleMailMessage);
    }

}