package com.yihangnav.web.controller;

import com.yihangnav.core.domain.SiteConfig;
import com.yihangnav.core.service.SiteConfigService;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/config")
public class ConfigController {

    private final SiteConfigService siteConfigService;

    public ConfigController(SiteConfigService siteConfigService) {
        this.siteConfigService = siteConfigService;
    }

    @GetMapping
    public ApiResponse<Map<String, String>> list() {
        List<SiteConfig> configs = siteConfigService.list();
        Map<String, String> map = configs.stream().collect(Collectors.toMap(SiteConfig::getConfigKey, SiteConfig::getConfigValue));
        return ApiResponse.ok(map);
    }

    @PostMapping
    public ApiResponse<Map<String, String>> save(@RequestBody Map<String, String> configs) {
        configs.forEach(siteConfigService::save);
        return list();
    }
}
