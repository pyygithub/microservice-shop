package com.wolf.exception;


import lombok.Data;

@Data
public class WeixinException extends RuntimeException{
    private int code = 1;
    private String msg;


    public WeixinException() {

    }

    public WeixinException(String msg) {
        super(msg);
    }

    public WeixinException(int code, String msg) {
        super(msg);
    }
}