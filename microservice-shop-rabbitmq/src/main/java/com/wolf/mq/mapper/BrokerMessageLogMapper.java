package com.wolf.mq.mapper;




import com.wolf.mq.model.BrokerMessageLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BrokerMessageLogMapper {
    /**
     * 添加消息记录数据
     * @param brokerMessageLog
     * @return
     */
    int insert(BrokerMessageLog brokerMessageLog);

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

    /**
     * 根据id删除
     * @param messageId
     */
    void delete(String messageId);

    /**
     * 修改
     * @param brokerMessageLog
     */
    void update(BrokerMessageLog brokerMessageLog);

    /**
     * 根据id查询
     * @param messageId
     * @return
     */
    BrokerMessageLog selectById(String messageId);

    /**
     * 多条件查询
     * @param queryParams
     * @return
     */
    List<BrokerMessageLog> selectListByParam(Map<String, Object> queryParams);
}
