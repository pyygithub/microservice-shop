package com.wolf.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;

import com.wolf.base.Result;
import com.wolf.constant.Constants;
import com.wolf.model.BrokerReceiveMessageLog;
import com.wolf.service.BrokerReceiveMessageLogService;
import com.wolf.util.IdGenerateUtil;
import com.wolf.util.RestCommonService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rabbitmq消费端
 */
@ConfigurationProperties(prefix = "mq")
@Component
public class RabbitmqConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqConsumer.class);

    @Autowired
    private RestCommonService restCommonService;

    @Autowired
    private BrokerReceiveMessageLogService brokerReceiveMessageLogService;

    /**
     * 消费端配置集合
     */
    private Map<String, List<String>> messageCodeMap = new HashMap<>();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${default.queue}", autoDelete = "false", durable = "true"),
            exchange = @Exchange(value = "${default.exchange}", type = ExchangeTypes.TOPIC),
            key = "${default.routingkey}"
    ))
    public void process(Message message, Channel channel) {
        try {
            log.info("执行消费...");

            JSONObject jsonMsg = JSON.parseObject(new String(message.getBody(), "utf-8"));

            String messageId = message.getMessageProperties().getMessageId();
            if (messageId == null) {
                log.error("消息缺失唯一标识,消费失败，请联系管理员");
                return;
            }
            String messageCode = jsonMsg.getString(Constants.MESSAGE_CODE);
            if ( messageCode != null && messageCodeMap.containsKey(messageCode)){
                // 根据消息中的messageCode获取消息编码，找到对应的接收接口
                List<String> interfaceUrls = messageCodeMap.get(jsonMsg.getString(Constants.MESSAGE_CODE));

                if(interfaceUrls != null && !interfaceUrls.isEmpty()) {
                    int count = 0;

                    // 循环调用消费接口
                    for(String url : interfaceUrls) {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        try {
                            // 调用消费接口
                            Result result = restCommonService.post(headers, jsonMsg, url);

                            // 业务接口消费mq消息异常
                            if(!"0".equals(result.getCode())) {
                                log.error("调用领域接口异常，url={}", url);

                                // 根据messageId查询消费异常数据记录
                                List<BrokerReceiveMessageLog> brokerReceiveMessageLogList = brokerReceiveMessageLogService.selectBrokerReceiveMessageLog(messageId);

                                // 如果数据库消费异常记录不存在
                                if(brokerReceiveMessageLogList == null || brokerReceiveMessageLogList.isEmpty()) {
                                    addBrokerReceiveMessageLog(jsonMsg, messageId, url, result.getMsg());
                                }
                            } else {
                                log.info("接口消费成功：url={}", url);
                                count++;
                            }
                        } catch (Exception e) {
                            log.error("调用消费接口异常，请检测messageCode和地址映射配置:{}",e.getMessage());

                            // 根据messageId查询消费异常数据记录
                            List<BrokerReceiveMessageLog> brokerReceiveMessageLogList = brokerReceiveMessageLogService.selectBrokerReceiveMessageLog(messageId);

                            // 如果数据库消费异常记录不存在
                            if(brokerReceiveMessageLogList == null || brokerReceiveMessageLogList.isEmpty()) {
                                addBrokerReceiveMessageLog(jsonMsg, messageId, url, "调用领域消费接口异常，请检测messageCode和地址映射配置:" + e.getMessage());
                            }
                        }

                    }

                    // 如果所有接口消费成功：响应ack应答
                    if(count == interfaceUrls.size()) {
                        log.info("接口消费成功");
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    }
                    // 如果接口全部消费失败：响应nack应答，重新放入队列
                    else if(count == 0) {
                        // 根据messageId查询重回队列次数
                        int rePushCount = brokerReceiveMessageLogService.selectRePushCount(messageId);

                        // 重回队列次数大于等于3次，删除该消息
                        if(rePushCount >= 3) {
                            // 更新消息日志状态：status = 3
                            brokerReceiveMessageLogService.changeBrokerReceiveMessageLogStatusByMessageId(messageId, Constants.RE_PUSH_ERROR, new Date());

                            // 响应成功ACK，从队列中删除该消息
                            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                        } else {
                            // 根据messageId查询消费异常数据记录
                            List<BrokerReceiveMessageLog> brokerReceiveMessageLogList = brokerReceiveMessageLogService.selectBrokerReceiveMessageLog(messageId);

                            // 如果数据库消费异常记录不存在
                            if(brokerReceiveMessageLogList == null || brokerReceiveMessageLogList.isEmpty()) {
                                addBrokerReceiveMessageLog(jsonMsg, messageId, null, "接口全部消费异常，消息被系统重回队列");
                            }
                            //更新retryPushCount 重回队列次数
                            brokerReceiveMessageLogService.update4RePushReceive(messageId, new Date());

                            log.error("接口全部消费失败：响应nack应答，重新放入队列");
                            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                        }
                    }
                    else {
                        log.info("部分接口消费成功,消费失败接口系统会自动执行补偿策略,请注意查看日志信息");
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    }
                } else {
                    log.error("消费接口没在配置文件中配置，请联系管理员完成配置操作");
                }
            }

        } catch (Exception e) {
            log.error("Rabbitmq服务器异常，请联系管理员及时处理：{}", e.getMessage());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * 添加消费异常记录
     * @param jsonMsg
     * @param messageId
     * @param url
     * @param memo
     */
    private void addBrokerReceiveMessageLog(JSONObject jsonMsg, String messageId, String url, String memo) {
        // 记录错误消息及消息接口地址
        BrokerReceiveMessageLog receiveMessageLog = new BrokerReceiveMessageLog();

        receiveMessageLog.setId(IdGenerateUtil.getId());

        receiveMessageLog.setMessageId(messageId);

        // http://thc-enc-domain/xxxx
        String domainName = url.split("//")[1];

        domainName = domainName.substring(0, domainName.indexOf("/"));

        receiveMessageLog.setDomainName(domainName);

        receiveMessageLog.setUrl(url);

        receiveMessageLog.setMessageCode(jsonMsg.getString(Constants.MESSAGE_CODE));

        receiveMessageLog.setMessage(jsonMsg.toJSONString());

        receiveMessageLog.setStatus(Constants.RECEIVE_FAILURE);

        receiveMessageLog.setMemo(memo);

        receiveMessageLog.setTryCount(0);

        receiveMessageLog.setTryPushCount(0);

        // 设置消息消费失败后下次重新消费时间窗口大小：单位分钟
        receiveMessageLog.setNextRetry(DateUtils.addMinutes(new Date(), Constants.RE_RECEIVE_TIMEOUT));

        receiveMessageLog.setCreateTime(new Date());

        receiveMessageLog.setUpdateTime(new Date());

        // 记录消费失败数据日志
        brokerReceiveMessageLogService.insertBrokerReceiveMessageLog(receiveMessageLog);
    }

    public Map<String, List<String>> getMessageCodeMap() {
        return messageCodeMap;
    }

    public void setMessageCodeMap(Map<String, List<String>> messageCodeMap) {
        this.messageCodeMap = messageCodeMap;
    }
}
