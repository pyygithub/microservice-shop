package com.wolf.exception;


import lombok.Data;

@Data
public class MemberException extends RuntimeException{
    private int code = 1;
    private String msg;


    public MemberException() {

    }

    public MemberException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MemberException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}