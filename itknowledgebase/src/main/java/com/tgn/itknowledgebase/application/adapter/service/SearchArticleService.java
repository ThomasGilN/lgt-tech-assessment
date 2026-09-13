package com.tgn.itknowledgebase.application.adapter.service;

import com.tgn.itknowledgebase.application.model.ArticlePreview;
import com.tgn.itknowledgebase.application.port.in.usecase.SearchArticleUseCase;
import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;
import com.tgn.itknowledgebase.domain.model.ArticleMetadata;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchArticleService implements SearchArticleUseCase {

    private final ArticleRepository articleRepository;

    public SearchArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticlePreview> perform(SearchArticleRequest request) {

        final var limit = Math.min(request.limit(), 10);

        return articleRepository.findAllMetadata().stream()
                .map(metadata -> queryMatchDescriptionByStringSearch(
                        request.query(),
                        metadata
                ))
                .filter(match -> match.wordMatchCount() > 0)
                .sorted(Comparator
                        .comparingInt(QueryToDescriptionSearchMatch::wordMatchCount)
                        .reversed()
                        .thenComparing(match -> match.metadata().articleId()))
                .limit(limit)
                .map(QueryToDescriptionSearchMatch::metadata)
                .map(ArticlePreview::fromArticleMetadata)
                .toList();
    }

    private QueryToDescriptionSearchMatch queryMatchDescriptionByStringSearch(String query, ArticleMetadata metadata) {
        final var uniqueQueryWords = getWordsStream(query).collect(Collectors.toSet());
        final var wordMatchCount = (int) getWordsStream(metadata.description())
                .filter(uniqueQueryWords::contains)
                .count();

        return new QueryToDescriptionSearchMatch(
                metadata,
                wordMatchCount
        );
    }

    private Stream<String> getWordsStream(String string){
        return Arrays.stream(string.toLowerCase().split("\\s+"));
    }

    record QueryToDescriptionSearchMatch(ArticleMetadata metadata, int wordMatchCount) {}
}
