package com.wolf.mq.service;




import com.wolf.mq.model.BrokerReceiveMessageLog;
import com.wolf.mq.response.PageBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BrokerReceiveMessageLogService {
    /**
     * 插入Broker消息记录数据
     * @param brokerReceiveMessageLog
     */
    void insertBrokerReceiveMessageLog(BrokerReceiveMessageLog brokerReceiveMessageLog);

    /**
     * 删除Broker消息记录数据
     * @param messageId
     */
    void deleteBrokerReceiveMessageLog(String messageId);

    /**
     * 修改Broker消息记录数据
     * @param brokerReceiveMessageLog
     */
    void updateBrokerReceiveMessageLog(BrokerReceiveMessageLog brokerReceiveMessageLog);

    /**
     * 查询消息状态为1（消费失败）且已经超时的消息集合
     * @return
     */
    List<BrokerReceiveMessageLog> query4StatusAndTimeoutMessage();

    /**
     * 重新消费统计count次数 +1
     * @param id
     * @param updateTime
     */
    int update4RetryReceive(@Param("id") String id, @Param("updateTime") Date updateTime);

    /**
     * 重新push（重回队列）统计count次数 +1
     * @param messageId
     * @param updateTime
     */
    int update4RePushReceive(@Param("messageId") String messageId, @Param("updateTime") Date updateTime);

    /**
     * 更新最终消息消费结果 成功 or 失败
     * @param id
     * @param status
     * @param updateTime
     */
    int changeBrokerReceiveMessageLogStatusById(@Param("id") String id, @Param("status") String status, @Param("updateTime") Date updateTime);

    /**
     * 更新最终消息消费结果 成功 or 失败
     * @param messageId
     * @param status
     * @param updateTime
     */
    int changeBrokerReceiveMessageLogStatusByMessageId(@Param("messageId") String messageId, @Param("status") String status, @Param("updateTime") Date updateTime);


    /**
     * 查询Broker消息记录数据
     * @param messageId
     */
    List<BrokerReceiveMessageLog> selectBrokerReceiveMessageLog(String messageId);

    /**
     * 查询Broker消息记录数据
     * @param queryParams
     * @param pageNum
     * @param size
     */
    PageBean<BrokerReceiveMessageLog> selectBrokerReceiveMessageLog(Map<String, Object> queryParams, int pageNum, int size);

    /**
     * 查询重回队列次数
     * @param messageId
     * @return
     */
    int selectRePushCount(String messageId);
}
