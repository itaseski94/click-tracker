package com.google.appengine.clicktracker.form;

import java.util.ArrayList;
import java.util.List;

public class CampaignForm {

        private String redirectUrl;
        private List<String> platforms;


        public CampaignForm(String redirectUrl, ArrayList<String> platforms) {
            this.redirectUrl = redirectUrl;
            this.platforms = new ArrayList<String>(platforms);
        }

        public String getRedirectUrl() {
            return this.redirectUrl;
        }

        public ArrayList<String> getPlatforms() {
            return (ArrayList<String>) this.platforms;
        }

        private  CampaignForm() { }
}
