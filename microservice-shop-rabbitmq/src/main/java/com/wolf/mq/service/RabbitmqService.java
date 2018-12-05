package com.wolf.mq.service;

public interface RabbitmqService {

    /**
     * 带confirmCallback消息投递方法
     * @param domainName 领域名称
     * @param messageSecne 消息场景
     * @param message 消息内容
     */
    void confirmSend(String domainName, String messageSecne, String message);

    /**
     * 带confirmCallback消息投递方法
     * @param domainName 领域名称
     * @param messageSecne 消息场景
     * @param exchange 路由名称
     * @param routingkey 路由键
     * @param message 消息内容
     */
    void confirmSend(String domainName, String messageSecne, String exchange, String routingkey, String message);

}
