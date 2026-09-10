package com.tgn.itknowledgebase.domain.model;

import java.time.Instant;
import java.util.UUID;

public record ArticleMetadata(
    UUID articleId,
    String author,
    String title,
    String description,
    String path,
    String mediaType,
    Instant createdAt
){ }
