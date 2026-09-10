package com.tgn.itknowledgebase.application.adapter.service;

import com.tgn.itknowledgebase.application.port.in.usecase.CreateArticleUseCase;
import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;
import com.tgn.itknowledgebase.domain.model.Article;
import com.tgn.itknowledgebase.domain.model.ArticleMetadata;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateArticleServiceTest {

    @Test
    void createArticleServiceTest() {
        var repository = mock(ArticleRepository.class);
        var id = UUID.randomUUID();

        var metadata = mock(ArticleMetadata.class);
        var article = mock(Article.class);

        when(metadata.articleId()).thenReturn(id);
        when(metadata.title()).thenReturn("title");
        when(metadata.description()).thenReturn("content");

        when(article.metadata()).thenReturn(metadata);

        when(repository.save(any())).thenReturn(article);

        final var useCase = new CreateArticleService(repository);

        final var result = useCase.perform(new CreateArticleUseCase.CreateArticleRequest("author", "title", "content" ));

        assertEquals(id.toString(), result.id());
        assertEquals("title", result.title());
        assertEquals("content", result.description());
    }
}
