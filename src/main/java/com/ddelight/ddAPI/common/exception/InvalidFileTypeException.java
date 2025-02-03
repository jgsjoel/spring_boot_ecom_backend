package com.ddelight.ddAPI.common.exception;

public class InvalidFileTypeException extends RuntimeException{

    public InvalidFileTypeException(){
        super("File Should be jpg or png type.");
    }

}
