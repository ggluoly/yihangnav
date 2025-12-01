package com.yihangnav.core.service;

import com.yihangnav.core.domain.Card;
import com.yihangnav.core.domain.Category;
import com.yihangnav.core.repository.CardRepository;
import com.yihangnav.web.dto.CardMetadataResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> listByCategory(Category category) {
        return cardRepository.findAllByCategoryAndEnabledIsTrueOrderBySortOrderAscIdAsc(category);
    }

    public List<Card> search(String keyword) {
        return cardRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    public Optional<Card> findById(Long id) {
        return cardRepository.findById(id);
    }

    @Transactional
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Transactional
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }

    public CardMetadataResponse fetchMetadata(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (compatible; YihangNavBot/1.0)")
                .timeout(5000)
                .get();
        CardMetadataResponse resp = new CardMetadataResponse();
        resp.setUrl(url);
        resp.setTitle(firstNonBlank(
                doc.select("meta[property=og:title]").attr("content"),
                doc.title()));
        resp.setDescription(firstNonBlank(
                doc.select("meta[property=og:description]").attr("content"),
                doc.select("meta[name=description]").attr("content")));
        resp.setLogo(firstNonBlank(
                doc.select("meta[property=og:image]").attr("abs:content"),
                doc.select("link[rel~=(?i)icon]").attr("abs:href")));
        return resp;
    }

    private String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) {
                return v.trim();
            }
        }
        return null;
    }
}
