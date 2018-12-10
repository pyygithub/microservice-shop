package com.wolf.controller;

import com.wolf.base.Result;
import com.wolf.constants.Constants;
import com.wolf.feign.PayApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

@Controller
@Slf4j
public class PayController {

    @Autowired
    private PayApiFeign payApiFeign;

    // 使用payToken 进行支付
    @GetMapping("/aliPay")
    public void aliPay(@RequestParam String payToken, HttpServletResponse response, @RequestHeader HttpHeaders headers) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        // 1.参数验证
        if (StringUtils.isBlank(payToken)) {
            return;
        }
        // 2.调用支付服务接口 获取支付的form表单元素
        Result result = payApiFeign.payInfo(payToken, headers);
        if (!result.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            String msg = result.getMsg();
            log.info("####调用支付接口错误,code={}, msg={}#####", result.getCode(), msg);
            writer.println(msg);
            return;
        }
        // 3.返回可以执行的html元素给客户端
        LinkedHashMap data = (LinkedHashMap) result.getData();
        String payHtml = (String) data.get("payHtml");
        // 4.将payHtml写入到客户端
        writer.println(payHtml);
        writer.close();
    }
}