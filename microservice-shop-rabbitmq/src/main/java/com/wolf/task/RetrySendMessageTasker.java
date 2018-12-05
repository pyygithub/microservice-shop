package com.wolf.task;




import com.wolf.constant.Constants;
import com.wolf.model.BrokerMessageLog;
import com.wolf.producer.RabbitmqSender;
import com.wolf.service.BrokerMessageLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 *
 * 消息重试，最大努力尝试策略（定时任务）
 *
 *  每个5分钟拉取一下处于中间状态的消息，当然这个消息可以设置一个超时时间，比如超过1分钟status=0,
 *
 *  也就说明1分钟这个时间窗口内，我们的消息没有被确认，那么会按定时任务取出来）
 *
 *  接下来我们把中间状态的消息进行重新投递 retry send, 继续发送消息到MQ，当然可以有很多原因导致发送失败
 *
 *  我们可以采取设置最大努力尝试次数，比如投递了3次，还是失败，那么我们可以将最终状态设置为 status=2
 *
 *  最后交由 人工解决处理此类问题（或者把消息转存到失败表中）
 */

@Component
public class RetrySendMessageTasker {

    private static final Logger log = LoggerFactory.getLogger(RetrySendMessageTasker.class);

    @Autowired
    private RabbitmqSender rabbitmqSender;

    @Autowired
    private BrokerMessageLogService brokerMessageLogService;

    @Scheduled(initialDelay = 5000000, fixedDelay = 100000000)
    public void reSend() {
        //拉取 status=0 且 tomeout 超时的message
        List<BrokerMessageLog> list = brokerMessageLogService.query4StatusAndTimeoutMessage();

        list.forEach(messageLog -> {
            if(messageLog.getTryCount() >= 3) {

                //重试次数>=3 修改失败消息状态: status=2
                brokerMessageLogService.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constants.SEND_FAILURE, new Date());

            } else {
                //更新重新发送次数
                brokerMessageLogService.update4RetrySend(messageLog.getMessageId(), new Date());

                try {
                    // 重新发送
                    rabbitmqSender.sendMsg(messageLog.getMessageId(), messageLog.getExchangeName(), messageLog.getRoutingKeyName(), messageLog.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("消息重试发送异常");
                }
            }
        });
    }
}
