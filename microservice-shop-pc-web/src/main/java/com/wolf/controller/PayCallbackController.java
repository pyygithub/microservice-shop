package com.wolf.controller;

import com.wolf.base.Result;
import com.wolf.constants.Constants;
import com.wolf.feign.PayCallbackApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/alibaba/callBack")
public class PayCallbackController {
    private static final String PAY_SUCCESS = "pay_success";

    @Autowired
    private PayCallbackApiFeign payCallbackApiFeign;

    // 同步回调,解决隐藏参数
    @PostMapping("/synSuccessPage")
    public String synSuccessPage(HttpServletRequest request, String outTradeNo, String tradeNo, String totalAmount) {
        request.setAttribute("outTradeNo", outTradeNo);
        request.setAttribute("tradeNo", tradeNo);
        request.setAttribute("totalAmount", totalAmount);
        return PAY_SUCCESS;
    }


    // 同步回调
    @RequestMapping("/return_url")
    public void synCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("####进入同步回调方法#####");
        response.setContentType("text/html;charset=utf-8");
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<String, String>();

        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        PrintWriter writer = response.getWriter();

        log.info("####支付宝同步回调PayCallbackController.synCallBack开始, params={}####", params);
        Result synCallBack = payCallbackApiFeign.synCallBack(params);
        if (!synCallBack.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            return;
        }

        LinkedHashMap data = (LinkedHashMap) synCallBack.getData();
        String htmlForm = "<form name='punchout_form'"
                + " method='post' action='http://panyangyangoray.imwork.net/alibaba/callBack/synSuccessPage' >"
                + "<input type='hidden' name='outTradeNo' value='" + data.get("out_trade_no") + "'>"
                + "<input type='hidden' name='tradeNo' value='" + data.get("trade_no") + "'>"
                + "<input type='hidden' name='totalAmount' value='" + data.get("total_amount") + "'>"
                + "<input type='submit' value='立即支付' style='display:none'>"
                + "</form><script>document.forms[0].submit();" + "</script>";
        writer.println(htmlForm);
        writer.close();
    }


    @RequestMapping("/notify_url")
    @ResponseBody
    public String asynCallBack(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<String, String>();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return payCallbackApiFeign.asynCallBack(params);

    }
}