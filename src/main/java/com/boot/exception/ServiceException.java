package com.boot.exception;

import com.boot.common.base.BaseErrorCode;
import com.boot.common.enums.HttpStatus;

/**
 * 自定义异常
 */
public class ServiceException extends BaseException {

    public ServiceException(String msg) {
        super(msg);
    }
    /**
     * @param error 错误码
     */
    public ServiceException(BaseErrorCode error) {
        super(error.message(), error);
    }

    /**
     * @param msg   错误信息
     * @param error 错误码
     */
    public ServiceException(String msg, BaseErrorCode error) {
        super(msg, error);
    }

    public ServiceException(HttpStatus status) {
        super(new BaseErrorCode() {
            @Override
            public String code() {
                return status.getCode();
            }

            @Override
            public String message() {
                return status.getMsg();
            }

        });
    }
}
