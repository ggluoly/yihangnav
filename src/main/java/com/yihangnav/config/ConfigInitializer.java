package com.yihangnav.config;

import com.yihangnav.core.service.SiteConfigService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigInitializer implements CommandLineRunner {

    private final SiteConfigService siteConfigService;

    public ConfigInitializer(SiteConfigService siteConfigService) {
        this.siteConfigService = siteConfigService;
    }

    @Override
    public void run(String... args) {
        Map<String, String> defaults = new HashMap<>();
        defaults.put("site.title", "贻点导航");
        defaults.put("site.background", "/assets/images/bg.webp");
        defaults.put("ads.enabled", "true");
        defaults.forEach((k, v) -> siteConfigService.get(k).orElseGet(() -> siteConfigService.save(k, v)));
    }
}
