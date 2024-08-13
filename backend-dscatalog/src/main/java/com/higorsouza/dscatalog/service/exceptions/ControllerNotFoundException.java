package com.higorsouza.dscatalog.service.exceptions;

public class ControllerNotFoundException extends RuntimeException {

    public ControllerNotFoundException(String msg) {
        super(msg);
    }

}
