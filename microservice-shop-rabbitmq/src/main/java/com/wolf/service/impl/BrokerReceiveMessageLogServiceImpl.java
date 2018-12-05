package com.wolf.service.impl;


import com.github.pagehelper.PageHelper;

import com.wolf.mapper.BrokerReceiveMessageLogMapper;
import com.wolf.model.BrokerReceiveMessageLog;
import com.wolf.service.BrokerReceiveMessageLogService;
import com.wolf.response.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrokerReceiveMessageLogServiceImpl implements BrokerReceiveMessageLogService {

    private static final Logger log = LoggerFactory.getLogger(BrokerReceiveMessageLogServiceImpl.class);

    @Autowired
    private BrokerReceiveMessageLogMapper brokerReceiveMessageLogMapper;

    @Override
    public void insertBrokerReceiveMessageLog(BrokerReceiveMessageLog brokerReceiveMessageLog) {
        brokerReceiveMessageLogMapper.insert(brokerReceiveMessageLog);
    }

    @Override
    public void deleteBrokerReceiveMessageLog(String messageId) {
        brokerReceiveMessageLogMapper.delete(messageId);
    }

    @Override
    public void updateBrokerReceiveMessageLog(BrokerReceiveMessageLog brokerReceiveMessageLog) {
        brokerReceiveMessageLog.setUpdateTime(new Date());
        brokerReceiveMessageLogMapper.update(brokerReceiveMessageLog);
    }

    @Override
    public List<BrokerReceiveMessageLog> query4StatusAndTimeoutMessage() {
        return brokerReceiveMessageLogMapper.query4StatusAndTimeoutMessage();
    }

    @Override
    public int update4RetryReceive(String id, Date updateTime) {
        return brokerReceiveMessageLogMapper.update4RetryReceive(id, updateTime);
    }

    @Override
    public int update4RePushReceive(String messageId, Date updateTime) {
        return brokerReceiveMessageLogMapper.update4RePushReceive(messageId, updateTime);
    }

    @Override
    public int changeBrokerReceiveMessageLogStatusById(String id, String status, Date updateTime) {
        return brokerReceiveMessageLogMapper.changeBrokerReceiveMessageLogStatusById(id, status, updateTime);
    }

    @Override
    public int changeBrokerReceiveMessageLogStatusByMessageId(String messageId, String status, Date updateTime) {
        return brokerReceiveMessageLogMapper.changeBrokerReceiveMessageLogStatusByMessageId(messageId, status, updateTime);
    }


    @Override
    public List<BrokerReceiveMessageLog> selectBrokerReceiveMessageLog(String messageId) {
        return brokerReceiveMessageLogMapper.selectByMessageId(messageId);
    }

    @Override
    public PageBean<BrokerReceiveMessageLog> selectBrokerReceiveMessageLog(Map<String, Object> queryParams, int pageNum, int size) {
        PageHelper.startPage(pageNum, size);
        List<BrokerReceiveMessageLog> brokerReceiveMessageLogList = brokerReceiveMessageLogMapper.selectListByParam(queryParams);
        return new PageBean<>(brokerReceiveMessageLogList);
    }

    @Override
    public int selectRePushCount(String messageId) {
        Integer count = brokerReceiveMessageLogMapper.selectRePushCount(messageId);
        return count == null ? 0 : count.intValue();
    }
}
