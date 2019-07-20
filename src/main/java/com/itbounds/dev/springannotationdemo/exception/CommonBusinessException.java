package com.itbounds.dev.springannotationdemo.exception;

import org.springframework.http.HttpStatus;

/**
 * @Description TODO
 * @Author blake
 * @Date 2019-07-19 18:01
 * @Version 1.0
 */
public class CommonBusinessException extends RuntimeException {

    /**
     * 异常状态码
     */
    private int code = HttpStatus.BAD_REQUEST.value();

    /**
     * 异常描述信息
     */
    private String message;

    private Throwable cause;

    public CommonBusinessException() {
    }

    public CommonBusinessException(String message) {
        super(message);
        this.message = message;
    }

    public CommonBusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CommonBusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

