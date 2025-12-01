package com.yihangnav.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "nav_search_log")
public class SearchLog extends BaseEntity {

    @Column(nullable = false, length = 256)
    private String keyword;

    @Column(length = 64)
    private String ip;

    @Column(length = 256)
    private String userAgent;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
