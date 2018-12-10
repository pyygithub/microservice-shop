package com.wolf.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.wolf.api.PayCallbackApi;
import com.wolf.base.Result;
import com.wolf.config.AlipayConfig;
import com.wolf.constants.Constants;
import com.wolf.dao.PaymentInfoDao;
import com.wolf.entity.PaymentInfo;
import com.wolf.feign.OrderApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.Oneway;
import java.util.Map;

@RestController
@Slf4j
public class PayCallbackController implements PayCallbackApi{

    @Autowired
    private PaymentInfoDao paymentInfoDao;

    @Autowired
    private OrderApiFeign orderApiFeign;

    // 同步回调
    @Override
    public Result synCallBack(@RequestParam Map<String, String> params) {
        // 1.日志记录
        log.info("####支付宝同步通知synCallBack开始， params={}#####", params);
        try {
            // 2.验签操作
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
            log.info("####支付宝异步通知，signVerified={}", signVerified);
            if (!signVerified) {
                log.info("#####支付宝同步通知验签失败####");
                return Result.ERROR("验签失败");
            }
            //商户订单号
            String out_trade_no = new String(params.get("out_trade_no"));
            //支付宝交易号
            String trade_no = new String(params.get("trade_no"));
            //交易状态
            String trade_status = new String(params.get("trade_status"));
            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }

            JSONObject data = new JSONObject();
            data.put("outTradeNo", out_trade_no);
            data.put("tradeNo", trade_no);
            data.put("tradeStatus", trade_status);
            return Result.OK("验签成功", data);
        } catch (Exception e) {
            log.error("####支付宝同步通知异常,error={}#####", e);
            return Result.ERROR("系统错误");
        } finally {
            log.info("####支付宝同步通知synCallBack结束， params={}#####", params);
        }
    }

    // 异步回调
    @Override
    public String asynCallBack(@RequestParam Map<String, String> params) {
        // 获取支付宝GET过来反馈信息
        try {
            log.info("####异步回调开始####params:",params);
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key,
                    AlipayConfig.charset, AlipayConfig.sign_type); // 调用SDK验证签名

            if (!signVerified) {
                return Constants.PAY_FAIL;
            }
            // 商户订单号
            String outTradeNo = params.get("out_trade_no");

            // 调用订单数据库修改订单表数据库--处理幂等
            PaymentInfo paymentInfo = paymentInfoDao.getByOrderIdPayInfo(outTradeNo);
            if(paymentInfo==null){
                return Constants.PAY_FAIL;
            }
            // 判断是否已经支付过，如果已经支付过,返回success,防止重试产生重复
            Integer state = paymentInfo.getState();
            if (state.equals("1")) {
                return Constants.PAY_SUCCESS;
            }
            // 支付宝交易号
            String tradeNo = params.get("trade_no");
            // 交易状态
            String trade_status = params.get("trade_status");
            if (trade_status.equals("TRADE_SUCCESS")) {
                paymentInfo.setPayMessage(params.toString());
                paymentInfo.setPlatformorderId(tradeNo);
                paymentInfo.setState(1);
                Integer updatePayInfo = paymentInfoDao.updatePayInfo(paymentInfo);
                if (updatePayInfo <= 0) {
                    return Constants.PAY_FAIL;
                }

                // 调用订单接口通知修改 订单支付状态
                Result orderResult = orderApiFeign.updateOrder(1l, tradeNo, outTradeNo);
                if (!orderResult.getCode().equals(Constants.HTTP_RES_CODE_200)) {
                    return Constants.PAY_FAIL;
                }

            }
            return Constants.PAY_SUCCESS;
        } catch (Exception e) {
            log.info("######PayCallBackServiceImpl##ERROR:#####", e);
            return Constants.PAY_FAIL;
        }finally{
            log.info("####异步回调结束####params:",params);
        }

    }
}