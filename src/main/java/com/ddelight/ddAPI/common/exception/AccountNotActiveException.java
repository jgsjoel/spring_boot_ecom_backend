package com.ddelight.ddAPI.common.exception;

public class AccountNotActiveException extends RuntimeException{

    public AccountNotActiveException() {
        super("Account not active.");
    }

}
