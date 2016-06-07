package com.google.appengine.clicktracker.domain;


import java.util.*;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class Campaign {

    @Id
    @Index
    private long id;

    private String creator;

    private String redirectUrl;

    @Index private List<String> platforms;

    private String creationDate;

    private String updateDate;


    public Campaign(long id, String creator, String redirectUrl, ArrayList<String> platforms,
                    String creationDate, String updateDate) {
        this.id = id;
        this.creator = creator;
        this.redirectUrl = redirectUrl;
        this.platforms = new ArrayList<String>(platforms);
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    public void setRedirectUrl(String url) {
        this.redirectUrl = url;
    }

    public void setUpdateDate(String date) {
        this.updateDate = date;
    }
    public long getId() {
        return this.id;
    }

    public String getCreator() {
        return this.creator;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public ArrayList<String> getPlatforms() {
        return (ArrayList<String>) this.platforms;
    }

    public String getCreationDate() {
       return this.creationDate;
    }

    public String getUpdateDate() {
       return this.updateDate;
    }

    private Campaign() { }

}
