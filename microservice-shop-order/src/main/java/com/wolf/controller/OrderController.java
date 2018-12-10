package com.wolf.controller;

import com.wolf.api.OrderApi;
import com.wolf.base.Result;
import com.wolf.dao.OrderDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderController implements OrderApi{

    @Autowired
    private OrderDao orderDao;

    @Override
    public Result updateOrder(@RequestParam("isPay") Long isPay,
                              @RequestParam("payId")String payId,
                              @RequestParam("orderNumber") String orderNumber){
        int updateOrder = orderDao.updateOrder(isPay, payId, orderNumber);
        if (updateOrder <= 0) {
            return Result.ERROR("系统错误!");
        }
        return Result.OK();

    }
}