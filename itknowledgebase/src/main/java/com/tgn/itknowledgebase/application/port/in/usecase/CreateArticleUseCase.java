package com.tgn.itknowledgebase.application.port.in.usecase;

import com.tgn.itknowledgebase.application.model.ArticlePreview;

public interface CreateArticleUseCase {

    ArticlePreview perform(CreateArticleRequest request);

    record CreateArticleRequest(
            String author,
            String tittle,
            String content
    ){}
}
