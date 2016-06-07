package com.google.appengine.clicktracker.domain;


public class Redirect {

    private int statusCode;
    private String message;
    private String redirectUrl;

    public Redirect(int statusCode, String message, String redirectUrl) {
        this.statusCode = statusCode;
        this.message = message;
        this.redirectUrl = redirectUrl;
    }

    public int getStatusCode() {
        return this.statusCode;
    };

    public String getMessage() {
        return this.message;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

}
