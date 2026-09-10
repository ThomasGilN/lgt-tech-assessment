package com.tgn.itknowledgebase;

import com.tgn.itknowledgebase.application.port.in.usecase.CreateArticleUseCase;
import com.tgn.itknowledgebase.application.port.in.usecase.GetArticleUseCase;
import com.tgn.itknowledgebase.application.port.in.usecase.SearchArticleUseCase;
import com.tgn.itknowledgebase.application.port.out.repository.ArticleRepository;
import com.tgn.itknowledgebase.domain.IdGenerator;
import com.tgn.itknowledgebase.infraestructure.port.in.auth.GetCurrentAuthenticatedUser;
import com.tgn.itknowledgebase.infraestructure.port.in.mcp.ArticleTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ItKnowledgeBaseApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

	@Test
	void contextLoads() {}

    @Test
    void provideApplicationBeans() {
        assertNotNull(applicationContext.getBean(IdGenerator.class));
        assertNotNull(applicationContext.getBean(ArticleRepository.class));
        assertNotNull(applicationContext.getBean(SearchArticleUseCase.class));
        assertNotNull(applicationContext.getBean(GetArticleUseCase.class));
        assertNotNull(applicationContext.getBean(CreateArticleUseCase.class));
        assertNotNull(applicationContext.getBean(GetCurrentAuthenticatedUser.class));
        assertNotNull(applicationContext.getBean(ArticleTools.class));
    }

}
