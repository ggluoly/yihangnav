package com.yihangnav.web.dto;

public class CategoryUpdateRequest {
    private String name;
    private String description;
    private Integer sortOrder;
    private Long parentId;
    private boolean parentIdSet = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
        this.parentIdSet = true;
    }

    public boolean isParentIdSet() {
        return parentIdSet;
    }
}
