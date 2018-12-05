package com.wolf.exception;


import lombok.Data;

@Data
public class MessageException extends RuntimeException{
    private int code = 1;
    private String msg;


    public MessageException() {

    }

    public MessageException(String msg) {
        super(msg);
    }

    public MessageException(int code, String msg) {
        super(msg);
    }
}