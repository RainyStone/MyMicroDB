package com.microdb.exception;

/**
 * ParseException
 */
public class ParseException extends RuntimeException{
    private static final long serialVersionUID = -7697452912656585449L;

    public ParseException(){}

    public ParseException(String message,Throwable cause){
        super(message,cause);
    }
}
