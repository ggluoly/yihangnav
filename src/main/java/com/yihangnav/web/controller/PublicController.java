package com.yihangnav.web.controller;

import com.yihangnav.core.domain.AdSlot;
import com.yihangnav.core.domain.Category;
import com.yihangnav.core.service.AdSlotService;
import com.yihangnav.core.service.CardService;
import com.yihangnav.core.service.CategoryService;
import com.yihangnav.core.service.FriendLinkService;
import com.yihangnav.core.service.SiteConfigService;
import com.yihangnav.web.dto.ApiResponse;
import com.yihangnav.web.dto.HomeResponse;
import com.yihangnav.web.dto.NavCategoryDTO;
import com.yihangnav.web.dto.SearchEngineDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final CategoryService categoryService;
    private final CardService cardService;
    private final AdSlotService adSlotService;
    private final FriendLinkService friendLinkService;
    private final SiteConfigService siteConfigService;

    public PublicController(CategoryService categoryService, CardService cardService, AdSlotService adSlotService, FriendLinkService friendLinkService, SiteConfigService siteConfigService) {
        this.categoryService = categoryService;
        this.cardService = cardService;
        this.adSlotService = adSlotService;
        this.friendLinkService = friendLinkService;
        this.siteConfigService = siteConfigService;
    }

    @GetMapping("/home")
    public ApiResponse<HomeResponse> home() {
        HomeResponse response = new HomeResponse();
        response.setConfig(siteConfigService.list().stream()
                .collect(Collectors.toMap(c -> c.getConfigKey(), c -> c.getConfigValue())));
        response.getConfig().putIfAbsent("site.title", "贻点导航");
        response.getConfig().putIfAbsent("site.background", "/assets/images/bg.webp");
        response.getConfig().putIfAbsent("ads.enabled", "true");
        List<NavCategoryDTO> roots = categoryService.listRoot().stream()
                .map(this::buildTree)
                .collect(Collectors.toList());
        response.setCategories(roots);
        response.setAds(adSlotService.listEnabled());
        response.setFriends(friendLinkService.listAll());
        response.setSearchEngines(buildSearchEngines());
        return ApiResponse.ok(response);
    }

    @GetMapping("/search-engines")
    public ApiResponse<List<SearchEngineDTO>> searchEngines() {
        return ApiResponse.ok(buildSearchEngines());
    }

    private NavCategoryDTO buildTree(Category category) {
        NavCategoryDTO dto = NavCategoryDTO.from(category);
        dto.setCards(cardService.listByCategory(category));
        List<NavCategoryDTO> children = categoryService.listByParent(category.getId()).stream()
                .map(this::buildTree)
                .collect(Collectors.toList());
        dto.setChildren(children);
        return dto;
    }

    private List<SearchEngineDTO> buildSearchEngines() {
        return Arrays.asList(
                new SearchEngineDTO("Google", "全网搜索", "https://www.google.com/search?q=%s"),
                new SearchEngineDTO("Baidu", "百度一下", "https://www.baidu.com/s?wd=%s"),
                new SearchEngineDTO("Bing", "Bing Search", "https://www.bing.com/search?q=%s"),
                new SearchEngineDTO("GitHub", "GitHub Repos", "https://github.com/search?q=%s"),
                new SearchEngineDTO("站内搜索", "站内搜索", "/api/search?keyword=%s")
        );
    }
}
