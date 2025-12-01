package com.yihangnav.core.service;

import com.yihangnav.core.domain.SearchLog;
import com.yihangnav.core.repository.SearchLogRepository;
import org.springframework.stereotype.Service;

@Service
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    public SearchLogService(SearchLogRepository searchLogRepository) {
        this.searchLogRepository = searchLogRepository;
    }

    public void record(String keyword, String ip, String ua) {
        SearchLog log = new SearchLog();
        log.setKeyword(keyword);
        log.setIp(ip);
        log.setUserAgent(ua);
        searchLogRepository.save(log);
    }
}
