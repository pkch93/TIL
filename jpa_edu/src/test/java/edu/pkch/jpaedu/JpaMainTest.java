package edu.pkch.jpaedu;

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
    Member member;

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        member = new Member();
        member.setId("pkch");
        member.setUsername("park");
        member.setAge(27);

    }

    @Test
    void 생성_조회_테스트() {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            member_생성_조회();
            em.remove(member);
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
            member.setId("pkch93");

            Member foundMember = em.find(Member.class, "pkch93");

            em.remove(member);
            transaction.commit();
            assertNotEquals(member, foundMember);
            assertNull(foundMember);
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
            em.detach(member);
            Member updatedMember = em.find(Member.class, "pkch");
            em.remove(updatedMember);
            transaction.commit();

            assertEquals(20, updatedMember.getAge());
            assertNotNull(updatedMember);
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    private void member_생성_조회() {
        // given & when
        em.persist(member);
        Member foundMember = em.find(Member.class, "pkch");
        // then
        assertEquals(member, foundMember);
    }

    private void member_수정() {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        member_생성_조회();

        Member foundMember = em.find(Member.class, "pkch");
        foundMember.setAge(20);

        transaction.commit();
    }

    @AfterEach
    void tearDown() {
        em.close();
    }
}
