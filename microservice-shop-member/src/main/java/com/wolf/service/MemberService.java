package com.wolf.service;


import com.alibaba.fastjson.JSONObject;
import com.wolf.base.BaseRedisService;
import com.wolf.base.RestCommonService;
import com.wolf.base.Result;
import com.wolf.constants.Constants;
import com.wolf.dao.MemberDao;
import com.wolf.entity.UserEntity;
import com.wolf.exception.MemberException;
import com.wolf.utils.MD5Util;
import com.wolf.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@Slf4j
public class MemberService {

    private static final String MICROSERVICE_SHOP_RABBITMQ_HTTP_PREFIX = "http://microservice-shop-rabbitmq"; //服务名称

    private static final String MESSAGE_SECNE = "会员注册";// 场景名称

    @Value("${spring.application.name}")
    private String domainName;

    @Autowired
    private RestCommonService restCommonService;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private BaseRedisService baseRedisService;

    /**
     * 会员注册
     *
     * @param user
     * @param headers
     */
    public void register(@RequestBody UserEntity user, @RequestHeader HttpHeaders headers) {
        // 参数验证
        String password = user.getPassword();
        if (StringUtils.isBlank(password)) {
            throw new MemberException("密码不能为空");
        }

        // 密码MD5简单加密
        user.setPassword(MD5Util.MD5(password));
        int res = memberDao.insertUser(user);
        if (res <= 0) {
            throw new MemberException("用户注册失败");
        }

        // 向Rabbitmq推送消息
        sendMsg(user, headers);
    }

    private void sendMsg(UserEntity user, HttpHeaders headers) {
        try {
            JSONObject msg = new JSONObject();
            msg.put("messageCode", "email_msg");
            msg.put("email", user.getEmail());
            msg.put("username", user.getUsername());

            log.info("####会员服务推送消息到Rabbitmq#####");
            log.info("####消息内容：msg={}", msg);
            String url = MICROSERVICE_SHOP_RABBITMQ_HTTP_PREFIX + "/v1/rabbitmq/sendToDefaultExchange?domainName={domainName}&messageSecne={messageSecne}";
            Result result = restCommonService.post(headers, msg, url, domainName, MESSAGE_SECNE);
            if (result.getCode().equals(Constants.HTTP_RES_CODE_200)) {
                log.info("####消息推送Rabbitmq异常####,e={}", result.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("####会员推送消息异常####");
        }
    }

    /**
     * 用户登录
     *
     * @param user
     * @param headers
     * @return
     */
    public JSONObject login(UserEntity user, HttpHeaders headers) {
        // 1.验证参数
        String username = user.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new MemberException("用户名不能为空");
        }

        String password = user.getPassword();
        if (StringUtils.isBlank(password)) {
            throw new MemberException("用户密码不能为空");
        }
        // 2.数据库查询账号密码是否正确
        String newPassword = MD5Util.MD5(password);
        UserEntity userEntity = memberDao.login(username, newPassword);
        if (userEntity == null) {
            throw new MemberException("用户名或密码错误");
        }
        // 3.如果账号正确，对应生成token
        String memberToken = TokenUtil.getMemberToken();

        // 4.存放在redis中，key为token，value为userId
        Integer userId = userEntity.getId();
        log.info("#####用户信息存入到redis中：key={}, value={}", memberToken, userId);
        baseRedisService.setString(memberToken, userEntity.getId() + "", Constants.TOKEN_MEMBER_TIME);

        // 5.直接返回token
        JSONObject token = new JSONObject();
        token.put("token", memberToken);
        return token;
    }

    /**
     * 根据token 获取用户信息
     *
     *  补充：核心业务操作该方式就不是很安全
     *      1. 核心业务操作时可以采用手机验证码验证
     *      2. 记录登录IP或者绑定手机 和 token做匹配验证
     *
     * @param token
     * @param headers
     * @return
     */
    public UserEntity findUserByToken(String token, HttpHeaders headers) {
        // 1.验证参数
        if (StringUtils.isBlank(token)) {
            throw new MemberException("token不能为空");
        }
        // 2.从redis中使用token查询对应的userId
        String userId = baseRedisService.getString(token);
        if (StringUtils.isBlank(userId)) {
            throw new MemberException("token无效或者已经过期");
        }
        // 3.使用userId去数据库查询用户信息返回给客户端
        UserEntity userEntity = memberDao.findByID(Long.valueOf(userId));
        if (userEntity == null) {
            throw new MemberException("未查询到用户信息");
        }
        userEntity.setPassword(null);
        return  userEntity;
    }

    /**
     * 根据openId查询用户信息
     *
     * @param openId
     * @param headers
     * @return
     */
    public JSONObject findUserByOpenId(String openId, HttpHeaders headers) {
        // 1.验证参数
        if (StringUtils.isBlank(openId)) {
            throw new MemberException("系统错误！openid获取失败");
        }
        // 2.使用openid查询数据库对应的用户信息
        UserEntity userEntity = memberDao.findUserByOpenId(openId);
        if (userEntity == null) {
            throw new MemberException(Constants.HTTP_RES_CODE_201, "用户未授权QQ登录");
        }
        // 3.如果查询成，生成对应生成token
        String memberToken = TokenUtil.getMemberToken();

        // 4.存放在redis中，key为token，value为userId
        Integer userId = userEntity.getId();
        log.info("#####用户信息存入到redis中：key={}, value={}", memberToken, userId);
        baseRedisService.setString(memberToken, userEntity.getId() + "", Constants.TOKEN_MEMBER_TIME);

        // 5.直接返回token
        JSONObject token = new JSONObject();
        token.put("token", memberToken);
        return token;
    }

    /**
     * openid关联userId
     *
     * @param user
     * @param headers
     */
    public JSONObject openidRelationUserId(UserEntity user, HttpHeaders headers) {
        // 1.验证参数
        String username = user.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new MemberException("用户名不能为空");
        }
        String password = user.getPassword();
        if (StringUtils.isBlank(password)) {
            throw new MemberException("用户密码不能为空");
        }
        // 2.数据库查询账号密码是否正确
        String newPassword = MD5Util.MD5(password);
        UserEntity userEntity = memberDao.login(username, newPassword);
        if (userEntity == null) {
            throw new MemberException("用户名或密码错误");
        }
        // 3.如果登录成功，修改数据对应的openid
        Integer ret = memberDao.updateUserByOpenId(user.getOpenid(), userEntity.getId());
        if (ret <= 0 ) {
            throw new MemberException("QQ账号关联失败");
        }
        // 4.如果账号正确，对应生成token
        String memberToken = TokenUtil.getMemberToken();

        // 5.存放在redis中，key为token，value为userId
        Integer userId = userEntity.getId();
        log.info("#####用户信息存入到redis中：key={}, value={}", memberToken, userId);
        baseRedisService.setString(memberToken, userEntity.getId() + "", Constants.TOKEN_MEMBER_TIME);

        // 6.直接返回token
        JSONObject token = new JSONObject();
        token.put("token", memberToken);
        return token;
    }
}