package com.yihangnav.web.dto;

import com.yihangnav.core.domain.Card;
import com.yihangnav.core.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class NavCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private List<Card> cards = new ArrayList<>();
    private List<NavCategoryDTO> children = new ArrayList<>();

    public static NavCategoryDTO from(Category category) {
        NavCategoryDTO dto = new NavCategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<NavCategoryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<NavCategoryDTO> children) {
        this.children = children;
    }
}
