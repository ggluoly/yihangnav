package com.yihangnav.web.dto;

import com.yihangnav.core.domain.AdSlot;
import com.yihangnav.core.domain.FriendLink;

import java.util.List;
import java.util.Map;

public class HomeResponse {
    private Map<String, String> config;
    private List<NavCategoryDTO> categories;
    private List<AdSlot> ads;
    private List<FriendLink> friends;
    private List<SearchEngineDTO> searchEngines;

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public List<NavCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<NavCategoryDTO> categories) {
        this.categories = categories;
    }

    public List<AdSlot> getAds() {
        return ads;
    }

    public void setAds(List<AdSlot> ads) {
        this.ads = ads;
    }

    public List<FriendLink> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendLink> friends) {
        this.friends = friends;
    }

    public List<SearchEngineDTO> getSearchEngines() {
        return searchEngines;
    }

    public void setSearchEngines(List<SearchEngineDTO> searchEngines) {
        this.searchEngines = searchEngines;
    }
}
