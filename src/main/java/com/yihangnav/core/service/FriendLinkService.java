package com.yihangnav.core.service;

import com.yihangnav.core.domain.FriendLink;
import com.yihangnav.core.repository.FriendLinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendLinkService {

    private final FriendLinkRepository friendLinkRepository;

    public FriendLinkService(FriendLinkRepository friendLinkRepository) {
        this.friendLinkRepository = friendLinkRepository;
    }

    public List<FriendLink> listAll() {
        return friendLinkRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public Optional<FriendLink> findById(Long id) {
        return friendLinkRepository.findById(id);
    }

    @Transactional
    public FriendLink save(FriendLink link) {
        return friendLinkRepository.save(link);
    }

    @Transactional
    public void delete(Long id) {
        friendLinkRepository.deleteById(id);
    }
}
