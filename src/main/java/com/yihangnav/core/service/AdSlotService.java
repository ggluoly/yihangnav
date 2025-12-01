package com.yihangnav.core.service;

import com.yihangnav.core.domain.AdSlot;
import com.yihangnav.core.repository.AdSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdSlotService {

    private final AdSlotRepository adSlotRepository;

    public AdSlotService(AdSlotRepository adSlotRepository) {
        this.adSlotRepository = adSlotRepository;
    }

    public List<AdSlot> listEnabled() {
        return adSlotRepository.findAllByEnabledIsTrue();
    }

    public List<AdSlot> listAll() {
        return adSlotRepository.findAll();
    }

    public Optional<AdSlot> findById(Long id) {
        return adSlotRepository.findById(id);
    }

    @Transactional
    public AdSlot save(AdSlot slot) {
        return adSlotRepository.save(slot);
    }

    @Transactional
    public void delete(Long id) {
        adSlotRepository.deleteById(id);
    }
}
