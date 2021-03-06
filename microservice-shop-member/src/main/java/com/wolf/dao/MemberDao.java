package com.wolf.dao;

import com.wolf.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberDao {
    @Select("select  id,username,password,phone,email,created,updated from mb_user where id =#{userId}")
    UserEntity findByID(@Param("userId") Long userId);

    @Insert("INSERT  INTO mb_user  (username,password,phone,email,created,updated) VALUES (#{username}, #{password},#{phone},#{email}, now(), now());")
    Integer insertUser(UserEntity userEntity);

    @Insert("update  mb_user set openid = #{openid} , updated = now() where id = #{userId}")
    Integer updateUserByOpenId(@Param("openid") String openId, @Param("userId") Integer userId);

    @Select("select  id,username,password,phone,email,created,updated from mb_user where username =#{username} and password = #{password}")
    UserEntity login(@Param("username") String username, @Param("password") String password);

    @Select("select  id,username,password,phone,email,created,updated from mb_user where openid =#{openId}")
    UserEntity findUserByOpenId(@Param("openId") String openId);
}
