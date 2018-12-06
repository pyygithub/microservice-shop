package com.wolf.controller;


import com.wolf.base.Result;
import com.wolf.constants.Constants;
import com.wolf.feign.MemberApiFeign;
import com.wolf.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

@Controller
public class IndexController {

    private static final String INDEX_HOME = "index";

    @Autowired
    private MemberApiFeign memberApiFeign;

    /**
     * 主页
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, @RequestHeader HttpHeaders headers) {
        // 1.从cookie中获取token信息
        String token = CookieUtil.getUid(request, Constants.COOKIE_MEMBER_TOKEN);
        // 2.如果cookie 存在token 调用会员服务接口 使用token查询用户信息
        if (!StringUtils.isBlank(token)) {
            Result result = memberApiFeign.findUserByToken(token, headers);
            if (result.getCode().equals(Constants.HTTP_RES_CODE_200)) {
                LinkedHashMap userData = (LinkedHashMap) result.getData();
                String username = (String) userData.get("username");
                request.setAttribute("username", username);
            }
        }
        return INDEX_HOME;
    }
}