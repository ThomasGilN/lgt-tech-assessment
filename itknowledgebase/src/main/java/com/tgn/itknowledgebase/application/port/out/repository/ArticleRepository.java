package com.tgn.itknowledgebase.application.port.out.repository;

import com.tgn.itknowledgebase.domain.model.Article;
import com.tgn.itknowledgebase.domain.model.ArticleMetadata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository {

    List<ArticleMetadata> findAllMetadata();

    Optional<Article> findById(UUID id);

    Article save(SaveArticleRequest saveArticleRequest);

    record SaveArticleRequest(
            String author,
            String title,
            String content
    ) {
    }
}
