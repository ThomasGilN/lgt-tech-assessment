package com.tgn.itknowledgebase.domain.model;

public record Article (
        ArticleMetadata metadata,
        String content
) {
}
