package com.tgn.itknowledgebase.application.adapter.service;

import com.tgn.itknowledgebase.application.model.ArticlePreview;
import com.tgn.itknowledgebase.application.model.ArticlePreviewWithContent;
import com.tgn.itknowledgebase.application.port.in.usecase.GetArticleUseCase;
import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;
import com.tgn.itknowledgebase.domain.model.Article;

import java.util.Optional;

public class GetArticleService implements GetArticleUseCase {

    private final ArticleRepository articleRepository;

    public GetArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Optional<ArticlePreviewWithContent> perform(GerArticleRequest request) {
        return articleRepository.findById(request.articleId())
                .map(ArticlePreviewWithContent::fromArticle)
                .or(Optional::empty);

    }
}
