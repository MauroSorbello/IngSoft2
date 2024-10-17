package com.ej4.tinder.logic.error;

public class ErrorService extends Exception {
    public ErrorService(String msn) {
        super(msn);
    }
}
