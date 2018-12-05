package com.wolf.mq.mapper;




import com.wolf.mq.model.BrokerReceiveMessageLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BrokerReceiveMessageLogMapper {
    /**
     * 添加消息记录数据
     * @param brokerReceiveMessageLog
     * @return
     */
    int insert(BrokerReceiveMessageLog brokerReceiveMessageLog);

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
     * 根据id删除
     * @param messageId
     */
    void delete(String messageId);

    /**
     * 修改
     * @param brokerReceiveMessageLog
     */
    void update(BrokerReceiveMessageLog brokerReceiveMessageLog);

    /**
     * 根据id查询
     * @param messageId
     * @return
     */
    List<BrokerReceiveMessageLog> selectByMessageId(String messageId);

    /**
     * 多条件查询
     * @param queryParams
     * @return
     */
    List<BrokerReceiveMessageLog> selectListByParam(Map<String, Object> queryParams);

    /**
     * 查询重回队列次数
     * @param messageId
     * @return
     */
    Integer selectRePushCount(String messageId);
}
