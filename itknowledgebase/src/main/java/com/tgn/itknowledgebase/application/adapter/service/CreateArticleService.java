package com.tgn.itknowledgebase.application.adapter.service;

import com.tgn.itknowledgebase.application.model.ArticlePreview;
import com.tgn.itknowledgebase.application.port.in.usecase.CreateArticleUseCase;
import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;

public class CreateArticleService implements CreateArticleUseCase {

    private final ArticleRepository articleRepository;

    public CreateArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public ArticlePreview perform(CreateArticleRequest request) {
        final var saveArticleRequest = new ArticleRepository.SaveArticleRequest(
                request.author(),
                request.content(),
                request.tittle()
        );

        return ArticlePreview.fromArticleMetadata(articleRepository.save(saveArticleRequest).metadata());
    }
}
