package com.tgn.itknowledgebase.application.model;


import com.tgn.itknowledgebase.domain.model.Article;

public record ArticlePreviewWithContent(
        ArticlePreview articlePreview,
        String content
) {

    public static ArticlePreviewWithContent fromArticle(Article article) {
        return new ArticlePreviewWithContent(
                ArticlePreview.fromArticleMetadata(article.metadata()),
                article.content()
        );
    }
}
