package com.tgn.itknowledgebase.infraestructure.port.in.mcp;

import com.tgn.itknowledgebase.application.model.ArticlePreview;

import java.util.List;

public interface ArticleTools {

    List<ArticlePreview> searchArticles(String query, int limit);

    String getArticle(String articleId);

    ArticlePreview createArticle(String title, String content);


}
