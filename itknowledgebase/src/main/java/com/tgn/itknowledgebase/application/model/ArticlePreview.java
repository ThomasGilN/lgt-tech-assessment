package com.tgn.itknowledgebase.application.model;

import com.tgn.itknowledgebase.domain.model.ArticleMetadata;

public record ArticlePreview(
        String id,
        String title,
        String description
) {

    public static ArticlePreview fromArticleMetadata(ArticleMetadata metadata) {
        return new ArticlePreview(metadata.articleId().toString(), metadata.title(), metadata.description());
    }
}
