package com.google.appengine.clicktracker.domain;

public class Clicks {

    private String platform;
    private long clicks;

    public Clicks(String platform, long clicks) {
        this.platform = platform;
        this.clicks = clicks;
    }

    public String getPlatform() {
        return this.platform;
    }

    public long getClicks() {
        return this.clicks;
    }
}
