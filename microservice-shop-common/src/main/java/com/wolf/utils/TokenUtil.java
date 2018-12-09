package com.wolf.utils;


import com.wolf.constants.Constants;

import java.util.UUID;

public class TokenUtil {

    /**
     * 会员token生成器
     * @return
     */
    public static String getMemberToken() {
        return Constants.TOKEN_MEMBER + "-" + UUID.randomUUID();
    }

    /**
     * 支付token生成器
     * @return
     */
    public static String getPayToken() {
        return Constants.TOKEN_PAY + "-" + UUID.randomUUID();
    }
}