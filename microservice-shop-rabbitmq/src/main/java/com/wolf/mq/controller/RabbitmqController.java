package com.wolf.mq.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;


import com.wolf.common.base.Result;
import com.wolf.mq.service.RabbitmqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rabbitmq")
@Api(value = "RabbitmqController", description = "Rabbitmq服务")
public class RabbitmqController {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqController.class);

    @Autowired
    private RabbitmqService rabbitmqService;

    @ApiOperation(value = "Rabbitmq消息发送消息接口（默认exchange、routingkey)", notes = "Rabbitmq消息发送消息接口（默认exchange、routingkey)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "domainName", value = "领域名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "messageSecne", value = "消息场景", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "member", value = "消息内容", required = true, dataType = "String", paramType = "body"),


            @ApiImplicitParam(name = "TK_BUSINESS_SERIALID", value = "交易流水", required = false, dataType="String",paramType="header"),
            @ApiImplicitParam(name = "TK_REQUEST_SYS_CODE", value = "请求方系统编码", required = false, dataType="String",paramType="header"),
            @ApiImplicitParam(name = "TK_REQUEST_MODULE_CODE", value = "请求方模块编码", required = false, dataType="String",paramType="header"),
            @ApiImplicitParam(name = "TK_REQUEST_NODE_IP", value = "请求方节点IP", required = false, dataType="String",paramType="header"),
            @ApiImplicitParam(name = "Accept", value = "接收属性", required = true,dataType="String", paramType="header", defaultValue="application/json"),
            @ApiImplicitParam(name = "Accept-Charset", value = "接收字符集", required = true,dataType="String", paramType="header", defaultValue="utf-8"),
            @ApiImplicitParam(name = "Content-Type", value = "内容类型", required = true,dataType="String", paramType="header", defaultValue="application/json; charset=UTF-8")
    })
    @PostMapping("/sendToDefaultExchange")
    public Result send(@RequestParam String domainName, @RequestParam String messageSecne, @RequestBody JSONObject message, @RequestHeader HttpHeaders headers) {
        String msg = JSON.toJSONString(message, SerializerFeature.WriteMapNullValue);
        log.info("MQ发送报文:" + msg);
        rabbitmqService.confirmSend(domainName, messageSecne, msg);

        return Result.OK();
    }

    @ApiOperation(value = "Rabbitmq消息发送消息接口（指定exchange、routingkey)", notes = "Rabbitmq消息发送消息接口（指定exchange、routingkey)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "domainName", value = "领域名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "messageSecne", value = "消息场景", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "exchange", value = "交换机(exchange)名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "routingkey", value = "路由键名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "member", value = "消息内容", required = true, dataType = "String", paramType = "body"),


            @ApiImplicitParam(name = "TK_BUSINESS_SERIALID", value = "交易流水", required = false, dataType="String",paramType="header"),
            @ApiImplicitParam(name = "TK_REQUEST_SYS_CODE", value = "请求方系统编码", required = false, dataType="String",paramType="header"),
            @ApiImplicitParam(name = "TK_REQUEST_MODULE_CODE", value = "请求方模块编码", required = false, dataType="String",paramType="header"),
            @ApiImplicitParam(name = "TK_REQUEST_NODE_IP", value = "请求方节点IP", required = false, dataType="String",paramType="header"),
            @ApiImplicitParam(name = "Accept", value = "接收属性", required = true,dataType="String", paramType="header", defaultValue="application/json"),
            @ApiImplicitParam(name = "Accept-Charset", value = "接收字符集", required = true,dataType="String", paramType="header", defaultValue="utf-8"),
            @ApiImplicitParam(name = "Content-Type", value = "内容类型", required = true,dataType="String", paramType="header", defaultValue="application/json; charset=UTF-8")
    })
    @PostMapping("/sendToCustomExchange")
    public Result send(@RequestParam String domainName, @RequestParam String messageSecne,
                       @RequestParam String exchange,  @RequestParam String routingkey,
                       @RequestBody JSONObject message, @RequestHeader HttpHeaders headers) {

        String msg = JSON.toJSONString(message, SerializerFeature.WriteMapNullValue);
        log.info("MQ发送报文:" + msg);

        rabbitmqService.confirmSend(domainName, messageSecne, exchange, routingkey, msg);

        return Result.OK();
    }
}
