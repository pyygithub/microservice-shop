package com.wolf.mq.model;


import java.io.Serializable;
import java.util.Date;

public class BrokerReceiveMessageLog implements Serializable{
    /**
     * ID
     */
    private String id;
    /**
     * 消息唯一标识
     */
    private String messageId;
    /**
     * 领域名称
     */
    private String domainName;
    /**
     * 消费接口地址
     */
    private String url;
    /**
     * 消息码
     */
    private String messageCode;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 重试次数
     */
    private Integer tryCount;
    /**
     * 重回队列次数
     */
    private Integer tryPushCount;
    /**
     * 消息状态：0 消费成功 1 消费失败
     */
    private String status;

    /**
     * 消息异常原因备注
     */
    private String memo;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public Integer getTryPushCount() {
        return tryPushCount;
    }

    public void setTryPushCount(Integer tryPushCount) {
        this.tryPushCount = tryPushCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
