package edu.pkch.jpaedu;

import edu.pkch.jpaedu.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

public class JpaMainTest {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaedu");
    EntityManager em;
    User user;

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        user = new User();
        user.setId("pkch");
        user.setUsername("park");
        user.setAge(27);

    }

    @Test
    void 생성_조회_테스트() {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            member_생성_조회();
            em.remove(user);
            transaction.commit();
        } catch (Exception e) {

            transaction.rollback();
        }
    }

    @Test
    void 수정을_PersistenceContext가_관리하지않는_객체에_했을때_null_테스트() {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            member_생성_조회();
            user.setId("pkch93");

            User foundUser = em.find(User.class, "pkch93");

            em.remove(user);
            transaction.commit();
            assertNotEquals(user, foundUser);
            assertNull(foundUser);
        } catch (Exception e) {
            transaction.rollback();
        }
    } // 영속성이 관리하지 않는 객체로 변화를 유도했기에 변경이 되지 않는다.
    // 즉, pkch93은 Persistence Context가 가지고 있지 않으므로 foundMember는 null

    @Test
    void 수정을_PersistenceContext가_관리하는_객체에_했을때_테스트() {
        EntityTransaction transaction = em.getTransaction();
        try {
            member_수정();
            transaction.begin();
            em.detach(user);
            User updatedUser = em.find(User.class, "pkch");
            em.remove(updatedUser);
            transaction.commit();

            assertEquals(20, updatedUser.getAge());
            assertNotNull(updatedUser);
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    private void member_생성_조회() {
        // given & when
        em.persist(user);
        User foundUser = em.find(User.class, "pkch");
        // then
        assertEquals(user, foundUser);
    }

    private void member_수정() {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        member_생성_조회();

        User foundUser = em.find(User.class, "pkch");
        foundUser.setAge(20);

        transaction.commit();
    }

    @AfterEach
    void tearDown() {
        em.close();
    }
}
