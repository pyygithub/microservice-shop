package com.wolf.service;



import com.wolf.model.BrokerMessageLog;
import com.wolf.response.PageBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BrokerMessageLogService {
    /**
     * 插入Broker消息记录数据
     * @param brokerMessageLog
     */
    void insertBrokerMessageLog(BrokerMessageLog brokerMessageLog);

    /**
     * 删除Broker消息记录数据
     * @param messageId
     */
    void deleteBrokerMessageLog(String messageId);

    /**
     * 修改Broker消息记录数据
     * @param brokerMessageLog
     */
    void updateBrokerMessageLog(BrokerMessageLog brokerMessageLog);

    /**
     * 查询Broker消息记录数据
     * @param messageId
     */
    BrokerMessageLog selectBrokerMessageLog(String messageId);

    /**
     * 查询Broker消息记录数据
     * @param queryParams
     * @param pageNum
     * @param size
     */
    PageBean<BrokerMessageLog> selectBrokerMessageLog(Map<String, Object> queryParams, int pageNum, int size);

    /**
     * 查询消息状态为0（发送中）且已经超时的消息集合
     * @return
     */
    List<BrokerMessageLog> query4StatusAndTimeoutMessage();

    /**
     * 重新发送统计count发送次数 +1
     * @param messageId
     * @param updateTime
     */
    int update4RetrySend(@Param("messageId") String messageId, @Param("updateTime") Date updateTime);

    /**
     * 更新最终消息发送结果 成功 or 失败
     * @param messageId
     * @param status
     * @param updateTime
     */
    int changeBrokerMessageLogStatus(@Param("messageId") String messageId, @Param("status") String status, @Param("updateTime") Date updateTime);


}
