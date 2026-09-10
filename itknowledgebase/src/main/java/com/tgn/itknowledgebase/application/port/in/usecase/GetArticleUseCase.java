package com.tgn.itknowledgebase.application.port.in.usecase;

import com.tgn.itknowledgebase.application.model.ArticlePreviewWithContent;

import java.util.Optional;
import java.util.UUID;

public interface GetArticleUseCase {
    Optional<ArticlePreviewWithContent> perform(GerArticleRequest request);

    record GerArticleRequest(
            UUID articleId
    ){}
}
