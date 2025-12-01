package com.yihangnav.core.service;

import com.yihangnav.core.domain.SiteConfig;
import com.yihangnav.core.repository.SiteConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SiteConfigService {

    private final SiteConfigRepository siteConfigRepository;

    public SiteConfigService(SiteConfigRepository siteConfigRepository) {
        this.siteConfigRepository = siteConfigRepository;
    }

    public Optional<SiteConfig> get(String key) {
        return siteConfigRepository.findById(key);
    }

    public String getValue(String key, String defaultValue) {
        return siteConfigRepository.findById(key).map(SiteConfig::getConfigValue).orElse(defaultValue);
    }

    public List<SiteConfig> list() {
        return siteConfigRepository.findAll();
    }

    @Transactional
    public SiteConfig save(String key, String value) {
        return siteConfigRepository.save(new SiteConfig(key, value));
    }
}
