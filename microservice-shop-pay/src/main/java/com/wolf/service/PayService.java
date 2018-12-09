package com.wolf.service;

import com.alibaba.fastjson.JSONObject;
import com.wolf.base.BaseRedisService;
import com.wolf.base.Result;
import com.wolf.constants.Constants;
import com.wolf.dao.PaymentInfoDao;
import com.wolf.entity.PaymentInfo;
import com.wolf.exception.PayException;
import com.wolf.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class PayService {

    @Autowired
    private PaymentInfoDao paymentInfoDao;

    @Autowired
    private BaseRedisService baseRedisService;

    /**
     * 创建支付token令牌
     *
     * @param paymentInfo
     * @param headers
     * @return
     */
    public JSONObject getPayToken(PaymentInfo paymentInfo, HttpHeaders headers) {
        // 1.插入参数提交信息
        Integer savePaymentType = paymentInfoDao.savePaymentType(paymentInfo);
        if (savePaymentType <= 0) {
            throw new PayException("系统错误!");
        }
        // 2.生成对应token
        String payToken = TokenUtil.getPayToken();
        // 3.存放在redis中
        Integer payId = paymentInfo.getId();
        baseRedisService.setString(payToken, payId + "", Constants.PAY_TOKEN_MEMBER_TIME);
        // 4.返回token給消費者
        JSONObject result = new JSONObject();
        result.put("payToken", payToken);
        return result;
    }

    public void payInfo(String payToken) {
        // 1.参数验证
        if (StringUtils.isEmpty(payToken)) {
            throw new PayException("token不能为空!");
        }
        // 2.判断token有效期
        String payId = baseRedisService.getString(payToken);
        if (StringUtils.isEmpty(payId)) {
            throw new PayException("支付已经超时!");
        }
        // 3.使用payId 调用数据库查询对应的支付信息
        PaymentInfo paymentInfo = paymentInfoDao.getPaymentInfo(Long.parseLong(payId));
        if (paymentInfo == null) {
            throw new PayException("未找到支付信息.");
        }
        // 4.判断类型 调用 具体业务接口
        Long typeId = paymentInfo.getTypeId();
        PayManager payManager = null;
        // 调用支付接口 返回提交支付form表单元素给客户端
        if (typeId == 1) {
            payManager = aliBaBaManagerImpl;
        }
        try {
            String payInfo = payManager.payInfo(paymentInfo);
            JSONObject payInfoJSON = new JSONObject();
            payInfoJSON.put("payInfo", payInfo);
            return setResultSuccess(payInfoJSON);
        } catch (AlipayApiException e) {
            return setResultError("支付错误!");
        }

    }
}