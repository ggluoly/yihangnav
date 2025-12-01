package com.yihangnav.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "nav_ad_slot")
public class AdSlot extends BaseEntity {

    @Column(nullable = false, length = 32)
    private String position; // LEFT / RIGHT / TOP / BOTTOM

    @Column(nullable = false, length = 128)
    private String title;

    @Column(length = 512)
    private String imageUrl;

    @Column(length = 512)
    private String linkUrl;

    @Column(nullable = false)
    private Boolean enabled = true;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
