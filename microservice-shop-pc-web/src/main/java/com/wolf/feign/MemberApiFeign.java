package com.wolf.feign;

import com.wolf.api.MemberApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("microservice-shop-member")
public interface MemberApiFeign extends MemberApi{
}
