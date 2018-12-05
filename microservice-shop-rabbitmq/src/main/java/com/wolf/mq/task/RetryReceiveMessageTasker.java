package com.wolf.mq.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.wolf.common.base.Result;
import com.wolf.mq.constant.Constants;
import com.wolf.mq.model.BrokerReceiveMessageLog;
import com.wolf.mq.service.BrokerReceiveMessageLogService;
import com.wolf.common.utils.RestCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 *
 * 消息重试，最大努力尝试策略（定时任务）
 *
 *  每个5分钟拉取一下处于失败状态的消息，当然这个消息可以设置一个超时时间，比如超过1分钟status=1,
 *
 *  也就说明1分钟这个时间窗口内，我们的消息没有被消费，那么会按定时任务取出来）
 *
 *  接下来我们把失败状态的消息进行重新消费 retry receive, 继续调用业务接口，当然可以有很多原因导致发送失败
 *
 *  我们可以采取设置最大努力尝试次数，比如重试了3次，还是失败，那么我们可以将最终状态设置为 status=2
 *
 *  最后交由 人工解决处理此类问题（或者把消息转存到失败表中）
 */

@Component
public class RetryReceiveMessageTasker {

    private static final Logger log = LoggerFactory.getLogger(RetryReceiveMessageTasker.class);

    @Autowired
    private RestCommonService restCommonService;

    @Autowired
    private BrokerReceiveMessageLogService brokerReceiveMessageLogService;

    @Scheduled(initialDelay = 500000, fixedDelay = 1000000)
    public void reReceive() {
        //拉取 status=1 且 tomeout 超时的message
        List<BrokerReceiveMessageLog> list = brokerReceiveMessageLogService.query4StatusAndTimeoutMessage();
        if(list != null && !list.isEmpty()){
            list.forEach(messageLog -> {
                if(messageLog.getTryCount() >= 3) {
                    //重试次数>=3 修改失败消息状态: status=2
                    brokerReceiveMessageLogService.changeBrokerReceiveMessageLogStatusById(messageLog.getId(), Constants.RE_RECEIVE_ERROR, new Date());
                } else {
                    //更新retryCount消费次数
                    brokerReceiveMessageLogService.update4RetryReceive(messageLog.getId(), new Date());
                    try {
                        // 调用消费接口重新消费
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        JSONObject jsonMsg = JSON.parseObject(messageLog.getMessage());
                        Result result = restCommonService.post(headers, jsonMsg, messageLog.getUrl());
                        if(result != null && "0".equals(result.getCode())) {
                            // 修改消息状态为 成功
                            brokerReceiveMessageLogService.changeBrokerReceiveMessageLogStatusById(messageLog.getId(), Constants.RECEIVE_SUCESS, new Date());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("消息重试发送异常");
                    }
                }
            });
        }
    }
}
