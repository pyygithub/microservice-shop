package com.wolf.producer;



import com.wolf.constant.Constants;
import com.wolf.service.BrokerMessageLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Rabbitmq生产端
 */
@Component
@Transactional
public class RabbitmqSender {
    private static final Logger log = LoggerFactory.getLogger(RabbitmqSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogService brokerMessageLogService;

    @Value("${default.exchange}")
    private String exchange;

    @Value("${default.routingkey}")
    private String routingkey;

    // 回调函数： confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info("消息发送确认confirm：correlationData={}, ack={}, cause={}", correlationData, ack, cause);

            String messageId = correlationData.getId();
            if(ack) {
                //如果confirm返回成功，则进行更新
                brokerMessageLogService.changeBrokerMessageLogStatus(messageId, Constants.SEND_SUCCESS, new Date());
            } else {
                //失败则进行具体后续操作：重试 或者补偿等手段
                log.error("消息发送异常：系统会自动重试");
            }
        }
    };

    // return 路由不可达Listener
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
            log.error("消息路由不可达,可能是exchange或routingkey配置错误，请联系管理员!");
        }
    };

    /**
     * 向默认exchange发送消息
     * @param messageId
     * @param msg
     */
    public void sendMsg(String messageId, String msg) throws Exception {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //设置消息唯一ID
        CorrelationData correlationData = new CorrelationData(messageId);

        Message message = MessageBuilder
                .withBody(msg.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setMessageId(messageId)
                .build();

        //发送mq消息
        rabbitTemplate.convertAndSend(exchange, routingkey, message, correlationData);
    }

    /**
     * 向指定exchange和自定义routingkey发送消息
     * @param messageId
     * @param exchange
     * @param routingkey
     * @param msg
     */
    public void sendMsg(String messageId, String exchange, String routingkey, String msg) throws Exception {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //设置消息唯一ID
        CorrelationData correlationData = new CorrelationData(messageId);

        Message message = MessageBuilder
                .withBody(msg.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setMessageId(messageId)
                .build();

        //发送mq消息
        rabbitTemplate.convertAndSend(exchange, routingkey, message, correlationData);
    }
}
