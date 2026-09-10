package com.tgn.itknowledgebase.infraestructure.configuration;

import com.tgn.itknowledgebase.application.adapter.service.CreateArticleService;
import com.tgn.itknowledgebase.application.adapter.service.GetArticleService;
import com.tgn.itknowledgebase.application.adapter.service.SearchArticleService;
import com.tgn.itknowledgebase.application.port.in.usecase.CreateArticleUseCase;
import com.tgn.itknowledgebase.application.port.in.usecase.GetArticleUseCase;
import com.tgn.itknowledgebase.application.port.in.usecase.SearchArticleUseCase;
import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;
import com.tgn.itknowledgebase.domain.IdGenerator;
import com.tgn.itknowledgebase.domain.RandomUUIDGenerator;
import com.tgn.itknowledgebase.infraestructure.adapter.in.auth.SpringSecurityGetCurrentAuthenticatedUserAdapter;
import com.tgn.itknowledgebase.infraestructure.adapter.in.mcp.McpArticleTools;
import com.tgn.itknowledgebase.infraestructure.adapter.out.repository.InMemoryJsonArticleRepository;
import com.tgn.itknowledgebase.infraestructure.port.in.auth.GetCurrentAuthenticatedUser;
import com.tgn.itknowledgebase.infraestructure.port.in.mcp.ArticleTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;

@Configuration(proxyBeanMethods = false)
public class ApplicationBeansConfiguration {

    @Bean
    IdGenerator idGenerator() {
        return new RandomUUIDGenerator();
    }

    @Bean
    GetCurrentAuthenticatedUser getCurrentAuthenticatedUser() {
        return new SpringSecurityGetCurrentAuthenticatedUserAdapter();
    }

    @Bean
    ArticleRepository articleRepository(
            @Value("${itknowledgebase.dir.path}") String knowledgeBaseDirPath,
            ObjectMapper objectMapper,
            IdGenerator idGenerator) {
        return new InMemoryJsonArticleRepository(idGenerator, Path.of(knowledgeBaseDirPath), objectMapper);
    }

    @Bean
    SearchArticleUseCase searchArticlesUseCase(ArticleRepository repository) {
        return new SearchArticleService(repository);
    }

    @Bean
    GetArticleUseCase getArticleUseCase(ArticleRepository repository) {
        return new GetArticleService(repository);
    }

    @Bean
    CreateArticleUseCase createArticleUseCase(ArticleRepository repository) {
        return new CreateArticleService(repository);
    }

    @Bean
    ArticleTools articleTools(
            SearchArticleUseCase searchArticleUseCase,
            GetArticleUseCase getArticleUseCase,
            CreateArticleUseCase createArticleUseCase,
            GetCurrentAuthenticatedUser getCurrentAuthenticatedUser
    ) {
        return new McpArticleTools(searchArticleUseCase, getArticleUseCase, createArticleUseCase, getCurrentAuthenticatedUser);
    }


}
