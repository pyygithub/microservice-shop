package com.wolf.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wolf.exception.WeixinException;
import com.wolf.model.TextMessage;
import com.wolf.utils.CheckUtil;
import com.wolf.utils.HttpClientUtil;
import com.wolf.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class DispatcherController {

    private static final String QINGYUNKE_API = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";

    private static final String TULING_API = "http://openapi.tuling123.com/openapi/api/v2";

    private static final String TULING_APIKEY = "525effc75f874c53bcec9a7bfef5b395";

    /**
     * 微信服务器验证接口地址
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping("/dispatcher")
    public String dispatcher(String signature, String timestamp, String nonce, String echostr) {
        // 1.验证是否是微信来源
        boolean checkSignature = CheckUtil.checkSignature(signature, timestamp, nonce);
        // 2.如果是微信来源 返回 随机数echostr
        if (!checkSignature) {
            return null;
        }
        return echostr;
    }

    /**
     * 微信动作推送
     *
     * @param request
     * @return
     */
    @PostMapping("/dispatcher")
    public void dispatcherPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        // 1.将xml转换为map格式
        Map<String, String> mapResult = XmlUtil.parseXml(request);
        if (mapResult == null) {
            throw new WeixinException("类型转换错误");
        }
        // 2.判断消息类型
        String msgType = mapResult.get("MsgType");
        PrintWriter writer = response.getWriter();
        // 3.如果是文本消息，返回结果给微信服务器
        switch (msgType) {
            case "text":

                // 获取消息内容
                String content = mapResult.get("Content");
                // 发送消息
                String toUserName = mapResult.get("ToUserName");
                // 来自消息
                String fromUserName = mapResult.get("FromUserName");
                // 调用智能机器人接口
                //String requestResultJson = HttpClientUtil.doGet(QINGYUNKE_API + content);

                JSONObject param = new JSONObject();
                param.put("reqType", "0");
                JSONObject perception = new JSONObject();
                JSONObject text = new JSONObject();
                text.put("text", content);
                perception.put("inputText",text);
                param.put("perception",perception);

                JSONObject userInfo = new JSONObject();
                userInfo.put("apiKey",TULING_APIKEY);
                userInfo.put("userId", "12112");
                param.put("userInfo",userInfo);

                String json = param.toJSONString();
                String requestResultJson = HttpClientUtil.doPostJson(TULING_API, json);

                JSONObject jsonObject = new JSONObject().parseObject(requestResultJson);
                JSONArray result = jsonObject.getJSONArray("results");
                JSONObject values = null;
                String msg = null;
                if (result != null) {
                    values = result.getJSONObject(0).getJSONObject("values");
                    msg = values.getString("text") == null ? "" : values.getString("text");
                } else {
                    msg = "我也不知道回答什么！";
                }
                String resultTestMsg = setTextMess(msg, toUserName, fromUserName);
                writer.print(resultTestMsg);
                break;

            default:
                break;
        }
        writer.close();

    }

    public String setTextMess(String content, String fromUserName, String toUserName) {
        TextMessage textMessage = new TextMessage();
        textMessage.setFromUserName(fromUserName);
        textMessage.setToUserName(toUserName);
        textMessage.setContent(content);
        textMessage.setMsgType("text");
        textMessage.setCreateTime(new Date().getTime());
        String messageToXml = XmlUtil.messageToXml(textMessage);
        log.info("####setTextMess()###messageToXml:" + messageToXml);
        return messageToXml;
    }

}