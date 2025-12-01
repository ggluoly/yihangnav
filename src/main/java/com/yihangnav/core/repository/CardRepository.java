package com.yihangnav.core.repository;

import com.yihangnav.core.domain.Card;
import com.yihangnav.core.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByCategoryAndEnabledIsTrueOrderBySortOrderAscIdAsc(Category category);
    List<Card> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
