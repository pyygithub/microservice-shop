package com.wolf.controller;

import com.netflix.ribbon.proxy.annotation.Http;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;


@Controller
public class LoginController {

    private static final String LOGIN_VIEW = "login";

    private static final String INDEX_VIEW = "redirect:/";

    private static final String ERROR = "error";

    private static final String RELATION = "qqrelation";

    @Autowired
    private MemberApiFeign memberApiFeign;

    /**
     * 登录页面
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return LOGIN_VIEW;
    }

    /**
     * 登录
     *
     * @param userEntity
     * @param request
     * @param response
     * @param headers
     * @return
     */
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

    /**
     * 跳转到QQ授权地址
     *
     * @param request
     * @return
     * @throws QQConnectException
     */
    @RequestMapping("/locaQQLogin")
    public String locaQQLogin(HttpServletRequest request) throws QQConnectException {
        String authorizeURL = new Oauth().getAuthorizeURL(request);
        return "redirect:"+authorizeURL;
    }

    /**
     * 封装QQ授权回调地址
     *
     * @param request
     * @param response
     * @param httpSession
     * @return
     * @throws QQConnectException
     */
    @GetMapping("/qqLoginCallback")
    public String qqLoginCallback(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession, @RequestHeader HttpHeaders headers)
            throws QQConnectException {
        // 1.获取授权码 + 使用授权码获取accessToken
        AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(request);
        if (accessTokenObj == null) {
            request.setAttribute("error", "qq授权失败!");
            return ERROR;
        }
        String accessToken = accessTokenObj.getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            request.setAttribute("error", "qq授权失败!");
            return ERROR;
        }
        // 2.使用accessToken获取openid
        OpenID openIdObj = new OpenID(accessToken);
        String userOpenID = openIdObj.getUserOpenID();

        // 3.调用会员服务根据openId查询是否已经关联账号
        Result result = memberApiFeign.findUserByOpenId(userOpenID, headers);
        // 4. 如果用戶沒有关联QQ账号, 跳转到关联账号页面
        if (result.getCode().equals(Constants.HTTP_RES_CODE_201)) {
            // 将openid存入到session中
            httpSession.setAttribute(Constants.SESSION_QQ_OPENID, userOpenID);
            return RELATION;
        }
        // 5.如果用户关联账号 直接登录 将token信息存放到cookie中
        LinkedHashMap data = (LinkedHashMap) result.getData();
        String token = (String) data.get("token");
        if (StringUtils.isEmpty(token)) {
            request.setAttribute("error", "登录已经失效");
            return LOGIN_VIEW;
        }
        CookieUtil.addCookie(response, Constants.COOKIE_MEMBER_TOKEN, token, Constants.COOKIE_TOKEN_MEMBER_TIME);

        return INDEX_VIEW;
    }

    /**
     * QQ关联登录
     *
     * @param userEntity
     * @param request
     * @param response
     * @param headers
     * @return
     */
    @PostMapping("/qqRelation")
    public String qqRelation(UserEntity userEntity, HttpServletRequest request, HttpSession session, HttpServletResponse response, @RequestHeader HttpHeaders headers) {
        // 1.获取openId
        String openId = (String) session.getAttribute(Constants.SESSION_QQ_OPENID);
        if (StringUtils.isBlank(openId)) {
             request.setAttribute("error", "没有获取到openid");
             return ERROR;
        }
        userEntity.setOpenid(openId);
        // 2.调用关联接口，获取token信息
        Result result = memberApiFeign.qqRelation(userEntity, headers);
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