package com.yihangnav.web.controller;

import com.yihangnav.core.domain.AdSlot;
import com.yihangnav.core.service.AdSlotService;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/ads")
public class AdController {

    private final AdSlotService adSlotService;

    public AdController(AdSlotService adSlotService) {
        this.adSlotService = adSlotService;
    }

    @GetMapping
    public ApiResponse<List<AdSlot>> list() {
        return ApiResponse.ok(adSlotService.listAll());
    }

    @PostMapping
    public ApiResponse<AdSlot> create(@Valid @RequestBody AdSlot slot) {
        return ApiResponse.ok(adSlotService.save(slot));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdSlot>> update(@PathVariable Long id, @Valid @RequestBody AdSlot slot) {
        return adSlotService.findById(id)
                .map(existing -> {
                    slot.setId(existing.getId());
                    return ResponseEntity.ok(ApiResponse.ok(adSlotService.save(slot)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        adSlotService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
