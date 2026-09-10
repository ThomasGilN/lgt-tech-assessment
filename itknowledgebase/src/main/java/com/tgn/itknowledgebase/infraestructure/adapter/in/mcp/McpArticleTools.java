package com.tgn.itknowledgebase.infraestructure.adapter.in.mcp;

import com.tgn.itknowledgebase.application.model.ArticlePreview;
import com.tgn.itknowledgebase.application.model.ArticlePreviewWithContent;
import com.tgn.itknowledgebase.application.port.in.usecase.CreateArticleUseCase;
import com.tgn.itknowledgebase.application.port.in.usecase.GetArticleUseCase;
import com.tgn.itknowledgebase.application.port.in.usecase.SearchArticleUseCase;
import com.tgn.itknowledgebase.infraestructure.port.in.auth.GetCurrentAuthenticatedUser;
import com.tgn.itknowledgebase.infraestructure.port.in.mcp.ArticleTools;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;

import java.util.List;
import java.util.UUID;

public class McpArticleTools implements ArticleTools {

    private final SearchArticleUseCase searchArticleUseCase;
    private final GetArticleUseCase getArticleUseCase;
    private final CreateArticleUseCase createArticleUseCase;
    private final GetCurrentAuthenticatedUser getCurrentAuthenticatedUser;

    public McpArticleTools(SearchArticleUseCase searchArticleUseCase, GetArticleUseCase getArticleUseCase, CreateArticleUseCase createArticleUseCase, GetCurrentAuthenticatedUser getCurrentAuthenticatedUser) {
        this.searchArticleUseCase = searchArticleUseCase;
        this.getArticleUseCase = getArticleUseCase;
        this.createArticleUseCase = createArticleUseCase;
        this.getCurrentAuthenticatedUser = getCurrentAuthenticatedUser;
    }

    @Override
    @McpTool(
            name = "search_articles",
            description = "Search internal IT knowledge-base articles by keywords.",
            annotations = @McpTool.McpAnnotations(
                    readOnlyHint = true,
                    destructiveHint = false,
                    idempotentHint = true,
                    openWorldHint = false))
    public List<ArticlePreview> searchArticles(
            @McpToolParam(description = "Keywords to search for.") String query,
            @McpToolParam(description = "Maximum number of articles to return.") int limit
    ) {
        return searchArticleUseCase
                .perform(new SearchArticleUseCase.SearchArticleRequest(query, limit));
    }

    @Override
    @McpTool(
            name = "get_article",
            description = "Return the full text of an internal IT knowledge-base article.",
            annotations = @McpTool.McpAnnotations(
                    readOnlyHint = true,
                    destructiveHint = false,
                    idempotentHint = true,
                    openWorldHint = false))
    public String getArticle(
            @McpToolParam(description = "ID of the article to retrieve.")String articleId
    ) {
        return getArticleUseCase
                .perform(new GetArticleUseCase.GerArticleRequest(
                        UUID.fromString(articleId))
                )
                .map(ArticlePreviewWithContent::content)
                .orElse("");
    }

    @Override
    @McpTool(
            name = "create_article",
            description = "Create a new internal IT knowledge-base article.",
            annotations = @McpTool.McpAnnotations(
                    destructiveHint = false,
                    openWorldHint = false))
    public ArticlePreview createArticle(
            @McpToolParam(description = "Title of the new article.") String title,
            @McpToolParam(description = "Full text of the new article.") String content
    ) {
        final var currentUsername = getCurrentAuthenticatedUser.getUsername();
        return createArticleUseCase
                .perform(new CreateArticleUseCase.CreateArticleRequest(currentUsername,title, content));
    }
}
