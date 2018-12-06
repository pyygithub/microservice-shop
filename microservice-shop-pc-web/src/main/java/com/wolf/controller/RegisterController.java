package com.wolf.controller;

import com.netflix.discovery.converters.Auto;
import com.wolf.base.Result;
import com.wolf.constants.Constants;
import com.wolf.entity.UserEntity;
import com.wolf.feign.MemberApiFeign;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    private static final String REGISTER_VIEW = "register";

    private static final String LOGIN_VIEW = "login";

    @Autowired
    private MemberApiFeign memberApiFeign;

    /**
     * 跳转到注册页面
     */
    @GetMapping("/register")
    public String register() {
        return REGISTER_VIEW;
    }


    @PostMapping("/register")
    public String registerPost(UserEntity userEntity, HttpServletRequest request, @RequestHeader HttpHeaders headers) {
        // 1.验证参数
        String username = userEntity.getUsername();
        String password = userEntity.getPassword();
        if (StringUtils.isBlank(username)) {
            request.setAttribute("error", "用户名不能为空");
            return REGISTER_VIEW;
        }
        if (StringUtils.isBlank(password)) {
            request.setAttribute("error", "密码不能为空");
            return REGISTER_VIEW;
        }
        // 2.调用会员服务注册接口
        Result result = memberApiFeign.register(userEntity, headers);

        // 3.如果失败，跳转到失败页面
        if (!result.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            request.setAttribute("error", "注册失败");
            return REGISTER_VIEW;
        }

        // 4.如果成功，跳转到成功页面

        return REGISTER_VIEW;
    }
}