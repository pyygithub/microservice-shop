package com.wolf.api;

import com.wolf.base.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/order")
public interface OrderApi {

    @GetMapping("updateOrder")
    public Result updateOrder(@RequestParam("payState") Long payState, @RequestParam("payId")String payId, @RequestParam("orderNumber") String orderNumber);
}
