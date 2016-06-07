package com.google.appengine.clicktracker.service;

import com.google.appengine.clicktracker.domain.Campaign;
import com.google.appengine.clicktracker.domain.Platform;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {

    static {
        factory().register(Campaign.class);
        factory().register(Platform.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
