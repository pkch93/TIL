package edu.pkch.jpaedu;

import edu.pkch.jpaedu.domain.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;

public class RelationMappingTest extends JpaTest {

    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager = super.createEntityManager();
    }

    @Test
    void article_추가() {
        EntityTransaction tx = entityManager.getTransaction();

        try {
            tx.begin();
            Article article = new Article("title", "content");
            entityManager.persist(article);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
    }
}
