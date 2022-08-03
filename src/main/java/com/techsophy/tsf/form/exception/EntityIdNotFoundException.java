package com.techsophy.tsf.form.exception;

public class EntityIdNotFoundException extends RuntimeException
{
    final String errorCode;
    final String message;
    public EntityIdNotFoundException(String errorCode,String message)
    {
        super(message);
        this.errorCode=errorCode;
        this.message=message;
    }
}
