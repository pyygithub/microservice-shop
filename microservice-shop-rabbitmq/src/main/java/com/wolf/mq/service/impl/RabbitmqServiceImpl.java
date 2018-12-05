package com.wolf.mq.service.impl;




import com.wolf.constant.Constants;
import com.wolf.mapper.BrokerMessageLogMapper;
import com.wolf.model.BrokerMessageLog;
import com.wolf.mq.constant.Constants;
import com.wolf.mq.service.RabbitmqService;
import com.wolf.mq.util.IdGenerateUtil;
import com.wolf.producer.RabbitmqSender;
import com.wolf.service.RabbitmqService;
import com.wolf.util.IdGenerateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class RabbitmqServiceImpl implements RabbitmqService {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqServiceImpl.class);

    @Autowired
    private RabbitmqSender rabbitmqSender;

    @Value("${default.exchange}")
    private String exchange;

    @Value("${default.routingkey}")
    private String routingkey;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Override
    public void confirmSend(String domainName, String messageSecne, String message) {
        // 插入消息记录表数据
        String messageId = insertBrokerMessageLog(domainName, messageSecne, exchange, routingkey, message);

        // 发送消息
        try {
            rabbitmqSender.sendMsg(messageId, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void confirmSend(String domainName, String messageSecne, String exchange, String routingkey, String message) {
        // 插入消息记录表数据
        String messageId = insertBrokerMessageLog(domainName, messageSecne, exchange, routingkey, message);

        // 发送消息
        try {
            rabbitmqSender.sendMsg(messageId, exchange, routingkey, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入消息记录表数据
     * @param domainName
     * @param messageSecne
     * @param exchange
     * @param routingkey
     * @param message
     * @return
     */
    private String insertBrokerMessageLog(String domainName, String messageSecne, String exchange, String routingkey, String message) {
        String messageId = IdGenerateUtil.getId();

        // 插入消息记录表数据
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        // 消息唯一ID
        brokerMessageLog.setMessageId(messageId);
        // 领域名称
        brokerMessageLog.setDomainName(domainName);
        // 消息场景
        brokerMessageLog.setMessageSecne(messageSecne);
        // exchange名称
        brokerMessageLog.setExchangeName(exchange);
        // 路由键名称
        brokerMessageLog.setRoutingKeyName(routingkey);
        // 消息主体
        brokerMessageLog.setMessage(message);
        // 重试次数
        brokerMessageLog.setTryCount(0);
        // 设置状态为0 表示发送中
        brokerMessageLog.setStatus(Constants.SENDING);
        // 设置消息未确认超时时间窗口为 一分钟
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(new Date(), Constants.RE_SEND_TIMEOUT));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());

        // 插入消息记录表数据
        brokerMessageLogMapper.insert(brokerMessageLog);
        log.info("插入消息记录表数据成功");
        return messageId;
    }
}
