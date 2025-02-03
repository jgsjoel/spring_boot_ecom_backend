package com.ddelight.ddAPI.common.exception;

public class NoSuchImageException extends RuntimeException {
    public NoSuchImageException() {
        super("No such image");
    }
}
