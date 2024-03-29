package com.gamerental.common.api;

import org.axonframework.queryhandling.QueryExecutionException;

public class RentalQueryException extends QueryExecutionException {

    public RentalQueryException(String message, Throwable cause, Object details) {
        super(message, cause, details);
    }
}
