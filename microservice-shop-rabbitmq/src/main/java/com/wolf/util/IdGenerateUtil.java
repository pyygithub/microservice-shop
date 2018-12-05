package com.wolf.util;


import java.util.UUID;

/**
 * ID生成器
 */
public class IdGenerateUtil {

    /**
     * MQ消息ID
     * @return
     */
    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "");
    }


}
