package com.tgn.itknowledgebase.infrastructure.adapter.out.repository;

import com.tgn.itknowledgebase.domain.IdGenerator;
import com.tgn.itknowledgebase.domain.model.ArticleMetadata;
import com.tgn.itknowledgebase.infraestructure.adapter.out.repository.InMemoryJsonArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class InMemoryJsonArticleRepositoryTest {

    @TempDir
    Path dataDir;

    @Test
    void loadJsonDictionary() throws IOException {
        var idGenerator = mock(IdGenerator.class);

        Path articlesDirectory = Files.createDirectories(dataDir.resolve("articles"));
        Files.writeString(
                articlesDirectory.resolve("password-policy.md"),
                "# Password Policy",
                StandardCharsets.UTF_8);
        Files.writeString(
                dataDir.resolve("knowledge-base.json"),
                """
                        [
                            {
                                "articleId" : "6105b7aa-9511-4653-a29f-7fd6befdbd45",
                                "title" : "Password and MFA Recovery Policy",
                                "path" : "articles/password-and-mfa-policy.md",
                                "mediaType" : "text/markdown",
                                "description" : "Password reset, account lockout, and MFA device recovery procedures.",
                                "createdAt" : "2026-09-02T00:00:00Z",
                                "author" : "demo-seed"
                            }
                        ]
                        """,
                StandardCharsets.UTF_8);

        final var repository = new InMemoryJsonArticleRepository(
                idGenerator,
                dataDir,
                new ObjectMapper()
        );

        final var articles = repository.findAllMetadata();

        assertEquals(1, articles.size());
        final var expected = new ArticleMetadata(
                UUID.fromString("6105b7aa-9511-4653-a29f-7fd6befdbd45"),
                "demo-seed",
                "Password and MFA Recovery Policy",
                "Password reset, account lockout, and MFA device recovery procedures.",
                "articles/password-and-mfa-policy.md",
                "text/markdown",
                Instant.parse("2026-09-02T00:00:00Z")
        );

    }
}
