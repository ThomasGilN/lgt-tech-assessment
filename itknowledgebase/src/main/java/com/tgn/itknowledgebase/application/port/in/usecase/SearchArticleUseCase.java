package com.tgn.itknowledgebase.application.port.in.usecase;

import com.tgn.itknowledgebase.application.model.ArticlePreview;

import java.util.List;

public interface SearchArticleUseCase {

    List<ArticlePreview> perform(SearchArticleRequest request);

    record SearchArticleRequest(
            String query,
            int limit
    ) {}
}
