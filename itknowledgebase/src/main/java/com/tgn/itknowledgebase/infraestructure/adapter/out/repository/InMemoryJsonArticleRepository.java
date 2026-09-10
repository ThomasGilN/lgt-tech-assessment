package com.tgn.itknowledgebase.infraestructure.adapter.out.repository;

import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;
import com.tgn.itknowledgebase.domain.IdGenerator;
import com.tgn.itknowledgebase.domain.model.Article;
import com.tgn.itknowledgebase.domain.model.ArticleMetadata;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryJsonArticleRepository implements ArticleRepository {

    private static final String KNOWLEDGE_BASE_JSON_FILE_NAME = "knowledge-base.json";

    private final Map<UUID, ArticleMetadata> articleMetadataDictionary;
    private final IdGenerator idGenerator;
    private final Path knowledgeBaseDirPath;
    private final ObjectMapper objectMapper;

    public InMemoryJsonArticleRepository(IdGenerator idGenerator, Path knowledgeBaseDirPath, ObjectMapper objectMapper) {
        this.idGenerator = Objects.requireNonNull(idGenerator);
        this.knowledgeBaseDirPath = Objects.requireNonNull(knowledgeBaseDirPath);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.articleMetadataDictionary = loadArticlesMetadata();
    }

    @Override
    public List<ArticleMetadata> findAllMetadata() {
        return List.copyOf(articleMetadataDictionary.values());
    }

    @Override
    public Optional<Article> findById(UUID id) {

        final var metadata = articleMetadataDictionary.get(id);
        if (metadata == null) {
            return Optional.empty();
        }

        return Optional.of(
                new Article(
                        metadata,
                        readArticleContent(metadata)
                )
        );
    }

    @Override
    public Article save(SaveArticleRequest saveArticleRequest) {
        final var normalizedTitle = normalizeTitle(saveArticleRequest.title());
        final var metadata = new  ArticleMetadata(
                idGenerator.generateId(),
                saveArticleRequest.author(),
                normalizedTitle,
                generateDescription(saveArticleRequest.content()),
                generatePath(normalizedTitle),
                "text/markdown",
                Instant.now()
        );

        Path articleFile = knowledgeBaseDirPath.resolve(metadata.path());

        try {
            Files.createDirectories(articleFile.getParent());
            Files.writeString(
                    articleFile,
                    saveArticleRequest.content(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW);

            articleMetadataDictionary.put(metadata.articleId(), metadata);
            persistMetadata();
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Unable to create article content", exception);
        }

        return new Article(
                metadata,
                saveArticleRequest.content()
        );
    }

    private void persistMetadata() {
        Path metadataFile = knowledgeBaseDirPath.resolve(KNOWLEDGE_BASE_JSON_FILE_NAME);
        final var knowledgeBase = articleMetadataDictionary.values()
                .stream()
                .map(JsonArticleMetadataDTO::fromDomain)
                .toList();

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(metadataFile.toFile(), knowledgeBase);
    }

    private String normalizeTitle(String title) {
        return title.trim().replaceAll("\\s+", "-");
    }

    private String generatePath(String title) {
        return  "articles/" + title + ".md";
    }

    private String generateDescription(String content) {
        return content.trim().substring(0, Math.min(content.length(), 100));
    }

    private String readArticleContent(ArticleMetadata metadata) {
        Path articleFile = knowledgeBaseDirPath.resolve(metadata.path());
        try {
            return Files.readString(articleFile, StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Unable to read article content", exception);
        }
    }

    private Map<UUID, ArticleMetadata> loadArticlesMetadata() {
        Path metadataFile = knowledgeBaseDirPath.resolve(KNOWLEDGE_BASE_JSON_FILE_NAME);
        List<JsonArticleMetadataDTO> json = objectMapper.readValue(metadataFile.toFile(), new TypeReference<>() {});

        return json.stream()
                .map(JsonArticleMetadataDTO::toDomain)
                .collect(Collectors.toConcurrentMap(
                        ArticleMetadata::articleId,
                        article -> article,
                        (existing, replacement) -> existing,
                        ConcurrentHashMap::new
                ));
    }

    private record JsonArticleMetadataDTO(
            String articleId,
            String author,
            String title,
            String description,
            String path,
            String mediaType,
            String createdAt
    ) {

        private ArticleMetadata toDomain() {
            return new ArticleMetadata(
                    UUID.fromString(articleId),
                    author,
                    title,
                    description,
                    path,
                    mediaType,
                    Instant.parse(createdAt)
            );
        }

        private static JsonArticleMetadataDTO fromDomain(ArticleMetadata metadata) {
            return new JsonArticleMetadataDTO(
                    metadata.articleId().toString(),
                    metadata.author(),
                    metadata.title(),
                    metadata.description(),
                    metadata.path(),
                    metadata.mediaType(),
                    metadata.createdAt().toString()
            );
        }
    }
}
