package com.ddelight.ddAPI.common.exception;

public class JwtExpiredException extends RuntimeException{

    public JwtExpiredException(String message){
        super(message);
    }

}
