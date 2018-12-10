package com.wolf.feign;

import com.wolf.api.OrderApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("microservice-shop-order")
public interface OrderApiFeign extends OrderApi{
}
