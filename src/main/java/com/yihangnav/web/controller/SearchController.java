package com.yihangnav.web.controller;

import com.yihangnav.core.domain.Card;
import com.yihangnav.core.service.CardService;
import com.yihangnav.core.service.SearchLogService;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final CardService cardService;
    private final SearchLogService searchLogService;

    public SearchController(CardService cardService, SearchLogService searchLogService) {
        this.cardService = cardService;
        this.searchLogService = searchLogService;
    }

    @GetMapping
    public ApiResponse<List<Card>> search(@RequestParam String keyword,
                                          HttpServletRequest request,
                                          @RequestHeader(value = "User-Agent", required = false) String ua) {
        searchLogService.record(keyword, request.getRemoteAddr(), ua);
        return ApiResponse.ok(cardService.search(keyword));
    }
}
