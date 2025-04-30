package com.IngHubTurkey.digital_wallet.exception;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {

    private final int errorCode;

    public BusinessException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
