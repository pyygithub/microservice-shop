package com.wolf.mq.constant;

/**
 *  常量配置类
 */
public class Constants {

    public static final String SENDING = "0";  //投递中

    public static final String SEND_SUCCESS= "1";//投递成功

    public static final String SEND_FAILURE = "2"; //重新投递失败

    public static final int RE_SEND_TIMEOUT = 1;// 消息投递未确认超时时间，单位：分钟

    public static final String MESSAGE_CODE = "messageCode"; //MQ报文头:消息编码


    public static final String RECEIVE_SUCESS = "0"; //消费成功

    public static final String RECEIVE_FAILURE= "1"; //消费失败,消费接口系统异常

    public static final String RE_RECEIVE_ERROR = "2";// 消息重新消费失败

    public static final String RE_PUSH_ERROR = "3";// 消息重回队列消费失败

    public static final int RE_RECEIVE_TIMEOUT = 1;// 消息消费失败后重试超时时间，单位：分钟


}
