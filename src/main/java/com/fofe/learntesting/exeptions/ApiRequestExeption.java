package com.fofe.learntesting.exeptions;

public class ApiRequestExeption  extends RuntimeException{
    public ApiRequestExeption(String message) {
        super(message);
    }

    public ApiRequestExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
