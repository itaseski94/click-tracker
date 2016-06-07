package com.google.appengine.clicktracker.domain;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Platform {

    @Id
    private long id;

    @Index private String name;

    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Campaign> campaignKey;

    private int klicks;

    public Platform(long id, String name, long userId) {
        this.id = id;
        this.name = name;
        this.klicks = 0;
        this.campaignKey = Key.create(Campaign.class,userId);
    }

    public String getName() {
        return this.name;
    }

    public int getKlicks() {
        return this.klicks;
    }

    public void addKlick() {
        ++klicks;
    }

    public String toString() {
        return new String(name + ": " + klicks);
    }

    private Platform() { }
}
