package com.wolf.api;

import com.wolf.base.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@RequestMapping("/callback")
public interface PayCallbackApi {

    // 同步回调
    @GetMapping("/synCallBackService")
    public Result synCallBack(@RequestParam Map<String, String> params);

    // 异步回调
    @GetMapping("/asynCallBackService")
    public String asynCallBack(@RequestParam Map<String, String> params );

}