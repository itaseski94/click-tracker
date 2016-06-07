package com.google.appengine.clicktracker.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.clicktracker.Constants;
import com.google.appengine.clicktracker.domain.Campaign;
import com.google.appengine.clicktracker.domain.Clicks;
import com.google.appengine.clicktracker.domain.Platform;
import com.google.appengine.clicktracker.form.CampaignForm;
import com.google.appengine.clicktracker.domain.Redirect;

import static com.google.appengine.clicktracker.service.OfyService.factory;
import static com.google.appengine.clicktracker.service.OfyService.ofy;

import com.google.appengine.repackaged.com.google.common.base.Flag;
import com.google.appengine.repackaged.org.antlr.runtime.debug.Profiler;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

import java.text.DateFormat;
import java.util.*;



@Api(name = "tracker", version = "v1", scopes = {Constants.EMAIL_SCOPE },
        clientIds = {
                Constants.WEB_CLIENT_ID,
                Constants.API_EXPLORER_CLIENT_ID
        },
        description = "API's for the tracking RESTful Web Service")

public class TrackingAPI {

    @ApiMethod(name = "handleRequest", path = "client/campaign", httpMethod = HttpMethod.GET)

    public Redirect handleRequest(@Named("id") long id, @Named("platform") String platform) {
        Key key  =  Key.create(Campaign.class, id);
        Campaign campaign = (Campaign) ofy().load().key(key).now();
        if (campaign == null) {
            return new Redirect(301,"Moved Permanently","http://outfit7.com");
        }
        if (!campaign.getPlatforms().contains(platform)) {
            return new Redirect(301,"Moved Permanently","http://outfit7.com");
        }

        Platform p = ofy().load().type(Platform.class).ancestor(key).filter("name =", platform).first().now();
        p.addKlick();
        ofy().save().entity(p).now();

        return new Redirect(303,"See Other",campaign.getRedirectUrl());
    }

    @ApiMethod(name = "createCampaign", path = "admin/campaigns", httpMethod = HttpMethod.POST)

    public Campaign createCampaign(final User user, CampaignForm form) throws UnauthorizedException,
                                   BadRequestException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        if (form.getPlatforms() == null || form.getRedirectUrl() == null) {
            throw new BadRequestException("Malformed request body data");
        }
        Date creationDate = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String formattedDate = df.format(creationDate);

        Key<Campaign> key = factory().allocateId(Campaign.class);
        Campaign campaign = new Campaign(key.getId(),user.getEmail(),form.getRedirectUrl(),form.getPlatforms(),
                                         formattedDate,formattedDate);

        ofy().save().entity(campaign).now();
        for (String str: form.getPlatforms()) {
            Key<Platform> pk = factory().allocateId(key,Platform.class);
            Platform p = new Platform(pk.getId(),str,key.getId());
            ofy().save().entity(p).now();
        }
        return campaign;
    }

    @ApiMethod(name = "updateCampaign", path = "admin/campaigns/{id}", httpMethod = HttpMethod.PUT)

    public void updateCampaign(final User user, @Named("id") long id,
                               CampaignForm form) throws UnauthorizedException, NotFoundException, BadRequestException{
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        Key key = Key.create(Campaign.class, id);
        Campaign campaign = (Campaign) ofy().load().key(key).now();
        if (campaign == null) {
            throw new NotFoundException("Campaign not found");
        }
        Date creationDate = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        String formattedDate = df.format(creationDate);
        if (form.getPlatforms() == null && form.getRedirectUrl() == null) {
            throw new BadRequestException("Malformed request body data");
        }
        if (form.getPlatforms() != null) campaign.setRedirectUrl(form.getRedirectUrl());
        campaign.setUpdateDate(formattedDate);
        for (String str: form.getPlatforms()) {
            if (!campaign.getPlatforms().contains(str)) {
                campaign.getPlatforms().add(str);
            }
        }
        ofy().save().entity(campaign).now();
    }

    @ApiMethod(name = "deleteCampaign", path = "admin/campaigns/{id}", httpMethod = HttpMethod.DELETE)

    public void deleteCampaign(final User user, @Named("id") long id) throws UnauthorizedException,
                               NotFoundException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        Key key = Key.create(Campaign.class, id);
        Campaign campaign = (Campaign) ofy().load().key(key).now();
        if (campaign == null) {
            throw  new NotFoundException("Campaign not found");
        }

        Iterable<Key<Platform>> allKeys = ofy().load().type(Platform.class).ancestor(key).keys();
        ofy().delete().keys(allKeys);
        ofy().delete().key(key).now();
    }

    @ApiMethod(name = "getCampaign", path = "admin/campaigns", httpMethod = HttpMethod.GET)

    public Campaign getCampaign(final User user, @Named("id") long id) throws UnauthorizedException,
                                NotFoundException {
        if (user == null) {
           throw new UnauthorizedException("Authorization required");
        }

        Key key  =  Key.create(Campaign.class, id);
        Campaign campaign = (Campaign) ofy().load().key(key).now();
        if (campaign == null) {
            throw  new NotFoundException("Campaign not found");
        }

        return campaign;
    }

    @ApiMethod(name = "listPlatformCampaigns", path = "admin/campaigns/platform", httpMethod = HttpMethod.GET)

    public List<Campaign> listPlatformCampaigns(final User user, @Named("platform") String platform) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException(" ");
        }
        Query query = ofy().load().type(Campaign.class).order("__key__");
        query = query.filter("platforms =", platform);

        return query.list();
    }

    @ApiMethod(name = "getCampaignClicks", path = "admin/campaign/platform/clicks", httpMethod = HttpMethod.GET)

    public Clicks getCampaignClicks(final User user, @Named("id") long id, @Named("platform") String platform) throws
                                    UnauthorizedException, NotFoundException {
        Key key  =  Key.create(Campaign.class, id);
        Campaign campaign = (Campaign) ofy().load().key(key).now();
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        if (campaign == null || !campaign.getPlatforms().contains(platform)) {
            throw new NotFoundException("Resource not found");
        }

        Platform p = ofy().load().type(Platform.class).ancestor(key).filter("name =", platform).first().now();

        return new Clicks(p.getName(),p.getKlicks());
    }

    @ApiMethod(name = "getPlatformClicks", path = "admin/campaigns/platform/clicks")

    public Clicks getPlatformClicks(final User user, @Named("platform") String platform) throws UnauthorizedException, NotFoundException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        List<Platform> list = ofy().load().type(Platform.class).filter("name =", platform).list();
        if (list.isEmpty()) {
            throw new NotFoundException("Platform not found");
        }
        long sum = 0;
        for (Platform p : list) {
            sum += p.getKlicks();
        }

        return new Clicks(platform,sum);
    }
}
