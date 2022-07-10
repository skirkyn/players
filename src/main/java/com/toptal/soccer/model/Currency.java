package com.toptal.soccer.model;

public enum Currency {

    DOLLAR("$");

    private final String sign;

    Currency(final String sign){
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return this.sign;
    }
}
