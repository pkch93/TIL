package edu.pkch.jpaedu;

import edu.pkch.jpaedu.advanced.identify.Parent;
import edu.pkch.jpaedu.advanced.identify.ParentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IdentifyClassTest extends JpaTest {

    EntityManager em;

    @BeforeEach
    void setUp() {
        em = super.createEntityManager();
    }

    @Test
    void 복합키_IdClass_테스트() {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Parent parent = new Parent();
            parent.setId1("id1");
            parent.setId2("id2");
            parent.setName("hello");
            em.persist(parent);
            transaction.commit();

            // 조회
            transaction.begin();
            em.detach(parent);
            ParentId parentId = new ParentId("id1", "id2");
            Parent foundParent = em.find(Parent.class, parentId);
            transaction.commit();

            assertNotNull(foundParent);
            assertEquals("id1", foundParent.getId1());
            assertEquals("id2", foundParent.getId2());
        } catch (Exception e) {
            transaction.rollback();
        }
    }
}
