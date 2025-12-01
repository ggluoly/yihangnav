package com.yihangnav.core.repository;

import com.yihangnav.core.domain.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendLinkRepository extends JpaRepository<FriendLink, Long> {
    List<FriendLink> findAllByOrderBySortOrderAscIdAsc();
}
