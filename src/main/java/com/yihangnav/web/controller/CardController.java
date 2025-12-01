package com.yihangnav.web.controller;

import com.yihangnav.core.domain.Card;
import com.yihangnav.core.domain.Category;
import com.yihangnav.core.service.CardService;
import com.yihangnav.core.service.CategoryService;
import com.yihangnav.web.dto.ApiResponse;
import com.yihangnav.web.dto.CardMetadataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/cards")
public class CardController {

    private final CardService cardService;
    private final CategoryService categoryService;

    public CardController(CardService cardService, CategoryService categoryService) {
        this.cardService = cardService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Card>> create(@Valid @RequestBody Card card) {
        attachCategory(card);
        return ResponseEntity.ok(ApiResponse.ok(cardService.save(card)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Card>> update(@PathVariable Long id, @Valid @RequestBody Card card) {
        return cardService.findById(id)
                .map(existing -> {
                    card.setId(existing.getId());
                    attachCategory(card);
                    return ResponseEntity.ok(ApiResponse.ok(cardService.save(card)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/search")
    public ApiResponse<List<Card>> search(@RequestParam String keyword) {
        return ApiResponse.ok(cardService.search(keyword));
    }

    @GetMapping("/metadata")
    public ResponseEntity<ApiResponse<CardMetadataResponse>> fetchMetadata(@RequestParam String url) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(cardService.fetchMetadata(url)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("fetch metadata failed: " + e.getMessage()));
        }
    }

    private void attachCategory(Card card) {
        if (card.getCategory() != null && card.getCategory().getId() != null) {
            Category category = categoryService.findById(card.getCategory().getId()).orElse(null);
            card.setCategory(category);
        }
    }
}
