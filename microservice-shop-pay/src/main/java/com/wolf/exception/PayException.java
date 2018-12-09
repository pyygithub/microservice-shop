package com.wolf.exception;


import lombok.Data;

@Data
public class PayException extends RuntimeException{
    private int code = 1;
    private String msg;


    public PayException() {

    }

    public PayException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public PayException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}