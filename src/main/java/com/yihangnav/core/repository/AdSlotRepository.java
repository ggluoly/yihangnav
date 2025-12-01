package com.yihangnav.core.repository;

import com.yihangnav.core.domain.AdSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdSlotRepository extends JpaRepository<AdSlot, Long> {
    List<AdSlot> findAllByEnabledIsTrue();
}
