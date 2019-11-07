package edu.pkch.jpaedu;

import edu.pkch.jpaedu.domain.Article;
import edu.pkch.jpaedu.domain.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class OneToManyTest extends JpaTest {

    private EntityManager em;

    @BeforeEach
    void setUp() {
        em = super.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        Article article = new Article("hello", "world");
        em.persist(article);
        transaction.commit();
    }

    @Test
    @DisplayName("@OneToMany로 매핑했을 때 Insert와 Update 쿼리가 발생한다.")
    void findOneToMany() {
        EntityTransaction transaction = em.getTransaction();
        Article article = em.find(Article.class, 1L);

        try {
            transaction.begin();
            Comment comment = new Comment("hello");
            em.persist(comment);
            // comment insert 발생, 단, Comment에서는 Foreign Key를 관리하지 않으므로 Update 쿼리가 하나 더 발생
            article.getComments().add(comment); // Update 쿼리 발생
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }
}
