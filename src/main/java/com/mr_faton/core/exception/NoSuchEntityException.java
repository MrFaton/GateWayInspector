package com.mr_faton.core.exception;

/**
 * Description
 *
 * @author root
 * @since 11.11.2015
 */
public class NoSuchEntityException extends Exception{
    public NoSuchEntityException() {
    }

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchEntityException(Throwable cause) {
        super(cause);
    }

    public NoSuchEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
