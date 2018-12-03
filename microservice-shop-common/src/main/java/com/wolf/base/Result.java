package com.wolf.base;

import com.wolf.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口响应信息实体类
 *
 * @Author: wolf
 * @Date: 2018/12/3 20:04
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    public static Result OK() {
        return new Result(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
    }

    /**
     * 返回带data成功响应
     * @param data
     * @return
     */
    public static Result OK(Object data) {
        return new Result(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
    }

    /**
     * 返回指定msg和data成功响应
     * @param msg
     * @param data
     * @return
     */
    public static Result OK(String msg, Object data) {
        return new Result(Constants.HTTP_RES_CODE_200, msg, data);
    }

    /**
     * 返回默认错误响应
     * @return
     */
    public static Result ERROR() {
        return new Result(Constants.HTTP_RES_CODE_500, Constants.HTTP_RES_CODE_500_VALUE, null);
    }

    /**
     * 返回指定错误信息响应
     * @param msg
     * @return
     */
    public static Result ERROR(String msg) {
        return new Result(Constants.HTTP_RES_CODE_500, msg, null);
    }
}