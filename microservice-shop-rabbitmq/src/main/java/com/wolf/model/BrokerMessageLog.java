package com.wolf.model;


import java.io.Serializable;
import java.util.Date;

public class BrokerMessageLog implements Serializable{
    /**
     * 消息唯一标识
     */
    private String messageId;
    /**
     * 领域名称
     */
    private String domainName;
    /**
     * 消息场景
     */
    private String messageSecne;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 交换机名称
     */
    private String exchangeName;
    /**
     * 路由键名称
     */
    private String routingKeyName;
    /**
     * 重试次数
     */
    private Integer tryCount;
    /**
     * 消息状态：0 投递中 1 投递成功 2 投递失败
     */
    private String status;
    /**
     * 消息超时重试时间
     */
    private Date nextRetry;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    // setter 和 getter
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getMessageSecne() {
        return messageSecne;
    }

    public void setMessageSecne(String messageSecne) {
        this.messageSecne = messageSecne;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingKeyName() {
        return routingKeyName;
    }

    public void setRoutingKeyName(String routingKeyName) {
        this.routingKeyName = routingKeyName;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getNextRetry() {
        return nextRetry;
    }

    public void setNextRetry(Date nextRetry) {
        this.nextRetry = nextRetry;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
