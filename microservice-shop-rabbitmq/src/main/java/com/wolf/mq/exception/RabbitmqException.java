package com.wolf.mq.exception;

public class RabbitmqException extends RuntimeException{
    private String code;
    private String msg;

    public RabbitmqException() {}

    public RabbitmqException(String message) {
        super(message);
    }
    public RabbitmqException(String code, String message) {
        super(message);
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
