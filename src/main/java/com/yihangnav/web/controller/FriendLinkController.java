package com.yihangnav.web.controller;

import com.yihangnav.core.domain.FriendLink;
import com.yihangnav.core.service.FriendLinkService;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/friends")
public class FriendLinkController {

    private final FriendLinkService friendLinkService;

    public FriendLinkController(FriendLinkService friendLinkService) {
        this.friendLinkService = friendLinkService;
    }

    @GetMapping
    public ApiResponse<List<FriendLink>> list() {
        return ApiResponse.ok(friendLinkService.listAll());
    }

    @PostMapping
    public ApiResponse<FriendLink> create(@Valid @RequestBody FriendLink link) {
        return ApiResponse.ok(friendLinkService.save(link));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FriendLink>> update(@PathVariable Long id, @Valid @RequestBody FriendLink link) {
        return friendLinkService.findById(id)
                .map(existing -> {
                    link.setId(existing.getId());
                    return ResponseEntity.ok(ApiResponse.ok(friendLinkService.save(link)));
                })
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.<FriendLink>fail("friend link not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        friendLinkService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>ok(null));
    }
}
