package com.yihangnav.web.dto;

public class SearchEngineDTO {
    private String name;
    private String placeholder;
    private String urlTemplate;

    public SearchEngineDTO(String name, String placeholder, String urlTemplate) {
        this.name = name;
        this.placeholder = placeholder;
        this.urlTemplate = urlTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }
}
