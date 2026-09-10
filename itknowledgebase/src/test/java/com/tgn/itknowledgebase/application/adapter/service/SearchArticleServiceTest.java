package com.tgn.itknowledgebase.application.adapter.service;

import com.tgn.itknowledgebase.application.port.in.usecase.SearchArticleUseCase;
import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;
import com.tgn.itknowledgebase.domain.model.ArticleMetadata;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchArticleServiceTest {

    @Test
    void searchArticleTest()
    {
        ArticleRepository repository = mock(ArticleRepository.class);

        SearchArticleUseCase useCase = new SearchArticleService(repository);
        ArticleMetadata metadata1 = mock(ArticleMetadata.class);
        ArticleMetadata metadata2 = mock(ArticleMetadata.class);

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        when(metadata1.articleId()).thenReturn(id1);
        when(metadata1.title()).thenReturn("article 1");
        when(metadata1.description()).thenReturn("something match");
        when(metadata2.articleId()).thenReturn(id2);
        when(metadata2.title()).thenReturn("article 2");
        when(metadata2.description()).thenReturn("no match");

        when(repository.findAllMetadata()).thenReturn(List.of(
                metadata1,
                metadata2
        ));

        final var result = useCase.perform(new SearchArticleUseCase.SearchArticleRequest("something", 5));

        assertEquals(1, result.size());
        assertEquals("article 1", result.getFirst().title());

    }

    @Test
    void searchArticleTestNoMatch()
    {
        ArticleRepository repository = mock(ArticleRepository.class);

        SearchArticleUseCase useCase = new SearchArticleService(repository);
        ArticleMetadata metadata1 = mock(ArticleMetadata.class);
        ArticleMetadata metadata2 = mock(ArticleMetadata.class);

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        when(metadata1.articleId()).thenReturn(id1);
        when(metadata1.title()).thenReturn("article 1");
        when(metadata1.description()).thenReturn("no match");
        when(metadata2.articleId()).thenReturn(id2);
        when(metadata2.title()).thenReturn("article 2");
        when(metadata2.description()).thenReturn("no match");

        when(repository.findAllMetadata()).thenReturn(List.of(
                metadata1,
                metadata2
        ));

        final var result = useCase.perform(new SearchArticleUseCase.SearchArticleRequest("something", 5));

        assertEquals(0, result.size());
    }

    @Test
    void searchArticleTestLimitResult()
    {
        ArticleRepository repository = mock(ArticleRepository.class);

        SearchArticleUseCase useCase = new SearchArticleService(repository);
        ArticleMetadata metadata1 = mock(ArticleMetadata.class);
        ArticleMetadata metadata2 = mock(ArticleMetadata.class);

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        when(metadata1.articleId()).thenReturn(id1);
        when(metadata1.title()).thenReturn("article 1");
        when(metadata1.description()).thenReturn("something match");
        when(metadata2.articleId()).thenReturn(id2);
        when(metadata2.title()).thenReturn("article 2");
        when(metadata2.description()).thenReturn("something match");

        when(repository.findAllMetadata()).thenReturn(List.of(
                metadata1,
                metadata2
        ));

        final var result = useCase.perform(new SearchArticleUseCase.SearchArticleRequest("something", 1));

        assertEquals(1, result.size());
        assertEquals("article 1", result.getFirst().title());
    }

    @Test
    void searchArticleTestBetterMatch()
    {
        ArticleRepository repository = mock(ArticleRepository.class);

        SearchArticleUseCase useCase = new SearchArticleService(repository);
        ArticleMetadata metadata1 = mock(ArticleMetadata.class);
        ArticleMetadata metadata2 = mock(ArticleMetadata.class);

        var id1 = UUID.randomUUID();
        var id2 = UUID.randomUUID();

        when(metadata1.articleId()).thenReturn(id1);
        when(metadata1.title()).thenReturn("article 1");
        when(metadata1.description()).thenReturn("something  something match");
        when(metadata2.articleId()).thenReturn(id2);
        when(metadata2.title()).thenReturn("article 2");
        when(metadata2.description()).thenReturn("something match");

        when(repository.findAllMetadata()).thenReturn(List.of(
                metadata1,
                metadata2
        ));

        final var result = useCase.perform(new SearchArticleUseCase.SearchArticleRequest("something", 5));

        assertEquals(2, result.size());
        assertEquals("article 1", result.getFirst().title());
        assertEquals("article 2", result.getLast().title());
    }
}
