package com.wolf.feign;

import com.wolf.api.PayCallbackApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("microservice-shop-pay")
public interface PayCallbackApiFeign extends PayCallbackApi{
}
