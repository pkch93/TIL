package edu.pkch.jpaedu;

import edu.pkch.jpaedu.domain.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProxyTest extends JpaTest {

    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager = super.createEntityManager();
    }

    @Test
    void article_Proxy_접근_테스트() {
        Article found = entityManager.getReference(Article.class, 1L);
        assertNotNull(found);

        found.getContent();
    }

    @Test
    void article_Proxy_DB에_존재하지_않는_데이터_접근시_에러_테스트() {
        assertThrows(EntityNotFoundException.class, () -> {
            Article found = entityManager.getReference(Article.class, 2L);
            found.getContent();
        });
    }
}
