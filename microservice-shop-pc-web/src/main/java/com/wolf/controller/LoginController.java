package com.wolf.controller;

import com.wolf.base.Result;
import com.wolf.constants.Constants;
import com.wolf.entity.UserEntity;
import com.wolf.feign.MemberApiFeign;
import com.wolf.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;


@Controller
public class LoginController {

    private static final String LOGIN_VIEW = "login";

    private static final String INDEX_VIEW = "redirect:/";

    @Autowired
    private MemberApiFeign memberApiFeign;


    @GetMapping("/login")
    public String login() {
        return LOGIN_VIEW;
    }

    @PostMapping("/login")
    public String loginPost(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response, @RequestHeader HttpHeaders headers) {
        // 1.验证参数
        // 2.调用登录接口，获取token信息
        Result result = memberApiFeign.login(userEntity, headers);
        if (!result.getCode().equals(Constants.HTTP_RES_CODE_200)) {
            request.setAttribute("error", "账号或密码错误");
            return LOGIN_VIEW;
        }

        LinkedHashMap data = (LinkedHashMap) result.getData();
        String token = (String) data.get("token");
        if (StringUtils.isEmpty(token)) {
            request.setAttribute("error", "登录已经失效");
            return LOGIN_VIEW;
        }
        // 3.将token信息存放到cookie中
        CookieUtil.addCookie(response, Constants.COOKIE_MEMBER_TOKEN, token, Constants.COOKIE_TOKEN_MEMBER_TIME);

        return INDEX_VIEW;
    }
}