package com.boot.exception;

import com.boot.common.base.BaseErrorCode;

public class BaseException extends RuntimeException {
    public BaseErrorCode errorCode;

    public BaseException(BaseErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BaseException(String msg, BaseErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
