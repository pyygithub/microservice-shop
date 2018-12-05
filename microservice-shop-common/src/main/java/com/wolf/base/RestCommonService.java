package com.wolf.base;



import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import com.wolf.base.Result;
import com.wolf.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 功能描述：调用其它领域接口通用方法支持：GET POST PUT DELETE 请求类型,超时熔断
 *
 * @Author: itw_panyy
 * @Create Date: 2018-04-08 16:11
 * @Modified By: itw_panyy
 * @Modified Date: 2018-04-08 16:11
 */

@Service("mqRestCommonService")
public class RestCommonService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    /**
     * GET请求方法
     * @param headers   请求头
     * @param url       接口地址
     * @param uriVariables  路径参数列表
     * @return
     */
    @HystrixCommand(fallbackMethod = "getBack")
    public Result<?> get(HttpHeaders headers, String url, Object... uriVariables) {
        logger.info("URL:" + url);
        HttpEntity<?> requestEntity = new HttpEntity<String>(null, headers);
        ResponseEntity<Result<?>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                new ParameterizedTypeReference<Result<?>>(){}, uriVariables);

        return response.getBody();
    }

    /**
     * 方法get的回退方法
     */
    public Result<?> getBack(HttpHeaders headers, String url, Object... uriVariables) {
        logger.info("领域接口地址:{}", url);
        return new Result(Constants.HTTP_RES_CODE_500, "系统超时熔断", null);
    }

    /**
     * POST请求方法
     * @param headers   请求头
     * @param entity    post实体参数
     * @param url       接口地址
     * @param uriVariables  路径参数列表
     * @return
     */
    @HystrixCommand(fallbackMethod = "postBack")
    public Result<?> post(HttpHeaders headers, Object entity, String url, Object... uriVariables) {
        logger.info("领域接口地址:{}", url);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(entity, headers);
        ResponseEntity<Result<?>> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<Result<?>>(){}, uriVariables);

        return response.getBody();
    }

    /**
     * 方法post的回退方法
     */
    public Result<?> postBack(HttpHeaders headers, Object entity, String url, Object... uriVariables) {
        logger.info("领域接口地址:{}", url);
        return new Result(Constants.HTTP_RES_CODE_500, "系统超时熔断", null);
    }

    /**
     * PUT请求方法
     * @param headers   请求头
     * @param entity    post实体参数
     * @param url       接口地址
     * @param uriVariables  路径参数列表
     * @return
     */
    @HystrixCommand(fallbackMethod = "putBack")
    public Result<?> put(HttpHeaders headers, Object entity, String url, Object... uriVariables) {
        logger.info("领域接口地址:{}", url);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(entity, headers);
        ResponseEntity<Result<?>> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
                new ParameterizedTypeReference<Result<?>>(){}, uriVariables);

        return response.getBody();
    }

    /**
     * 方法put的回退方法
     */
    public Result<?> putBack(HttpHeaders headers, Object entity, String url, Object... uriVariables) {
        logger.info("领域接口地址:{}", url);
        return new Result(Constants.HTTP_RES_CODE_500, "系统超时熔断", null);
    }


    /**
     * DELETE请求方法
     * @param headers   请求头
     * @param entity    post实体参数
     * @param url       接口地址
     * @param uriVariables  路径参数列表
     * @return
     */
    @HystrixCommand(fallbackMethod = "deleteBack")
    public Result<?> delete(HttpHeaders headers, Object entity, String url, Object... uriVariables) {
        logger.info("领域接口地址:{}", url);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(entity, headers);
        ResponseEntity<Result<?>> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity,
                new ParameterizedTypeReference<Result<?>>(){}, uriVariables);

        return response.getBody();
    }

    /**
     * 方法delete的回退方法
     */
    public Result<?> deleteBack(HttpHeaders headers, Object entity, String url, Object... uriVariables) {
        logger.info("领域接口地址:{}", url);
        return new Result(Constants.HTTP_RES_CODE_500, "系统超时熔断", null);
    }


}
