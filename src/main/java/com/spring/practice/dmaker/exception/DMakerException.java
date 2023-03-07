package com.spring.practice.dmaker.exception;

import lombok.Getter;

@Getter
public class DMakerException extends RuntimeException{
    private DMakerErrorCode dMakerErrorCode;
    private String detail;

    public DMakerException(DMakerErrorCode errorCode) {
        super(errorCode.getMessage());
        this.dMakerErrorCode = errorCode;
        this.detail = errorCode.getMessage();
    }

    public DMakerException(DMakerErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.dMakerErrorCode = errorCode;
        this.detail = detail;
    }
}
