package com.tgn.itknowledgebase.application.adapter.service;

import com.tgn.itknowledgebase.application.port.in.usecase.GetArticleUseCase;
import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;
import com.tgn.itknowledgebase.domain.model.Article;
import com.tgn.itknowledgebase.domain.model.ArticleMetadata;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetArticleServiceTest {

    @Test
    void testGetArticleServiceTest()
    {
        final var id = UUID.randomUUID();
        final var content = "content";

        ArticleRepository repository = mock(ArticleRepository.class);
        GetArticleUseCase useCase = new GetArticleService(repository);

        var metadata = mock(ArticleMetadata.class);
        var article = mock(Article.class);

        when(metadata.articleId()).thenReturn(id);
        when(metadata.title()).thenReturn("article 1");
        when(metadata.description()).thenReturn("description");

        when(article.metadata()).thenReturn(metadata);
        when(article.content()).thenReturn(content);

        when(repository.findById(id)).thenReturn(Optional.of(article));

        final var optionalResult = useCase.perform(new GetArticleUseCase.GerArticleRequest(id));

        assertTrue(optionalResult.isPresent());

        final var result = optionalResult.get();
        assertEquals(id.toString(), result.articlePreview().id());
        assertEquals(content, result.content());
    }

    @Test
    void testGetArticleServiceTestInvalidId()
    {
        final var id = UUID.randomUUID();

        ArticleRepository repository = mock(ArticleRepository.class);
        GetArticleUseCase useCase = new GetArticleService(repository);

        when(repository.findById(id)).thenReturn(Optional.empty());

        final var optionalResult = useCase.perform(new GetArticleUseCase.GerArticleRequest(id));

        assertTrue(optionalResult.isEmpty());

    }
}
