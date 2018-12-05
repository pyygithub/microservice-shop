package com.wolf.service.impl;


import com.github.pagehelper.PageHelper;


import com.wolf.mapper.BrokerMessageLogMapper;
import com.wolf.model.BrokerMessageLog;
import com.wolf.service.BrokerMessageLogService;
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
public class BrokerMessageLogServiceImpl implements BrokerMessageLogService {

    private static final Logger log = LoggerFactory.getLogger(BrokerMessageLogServiceImpl.class);

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Override
    public void insertBrokerMessageLog(BrokerMessageLog brokerMessageLog) {
        brokerMessageLogMapper.insert(brokerMessageLog);
    }

    @Override
    public void deleteBrokerMessageLog(String messageId) {
        brokerMessageLogMapper.delete(messageId);
    }

    @Override
    public void updateBrokerMessageLog(BrokerMessageLog brokerMessageLog) {
        brokerMessageLog.setUpdateTime(new Date());
        brokerMessageLogMapper.update(brokerMessageLog);
    }

    @Override
    public BrokerMessageLog selectBrokerMessageLog(String messageId) {
        return brokerMessageLogMapper.selectById(messageId);
    }

    @Override
    public PageBean<BrokerMessageLog> selectBrokerMessageLog(Map<String, Object> queryParams, int pageNum, int size) {
        PageHelper.startPage(pageNum, size);
        List<BrokerMessageLog> brokerMessageLogList = brokerMessageLogMapper.selectListByParam(queryParams);
        return new PageBean<>(brokerMessageLogList);
    }

    @Override
    public List<BrokerMessageLog> query4StatusAndTimeoutMessage() {
        return brokerMessageLogMapper.query4StatusAndTimeoutMessage();
    }

    @Override
    public int update4RetrySend(String messageId, Date updateTime) {
        return brokerMessageLogMapper.update4RetrySend(messageId, updateTime);
    }

    @Override
    public int changeBrokerMessageLogStatus(String messageId, String status, Date updateTime) {
        return brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, status, updateTime);
    }
}
