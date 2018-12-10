package com.wolf.feign;

import com.wolf.api.PayApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("microservice-shop-pay")
public interface PayApiFeign extends PayApi{

}