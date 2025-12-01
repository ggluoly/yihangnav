package com.yihangnav.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "nav_category")
@JsonIgnoreProperties(value = {"parent"}, allowSetters = true)
public class Category extends BaseEntity {

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 128)
    private String description;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @ManyToOne
    private Category parent;

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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
